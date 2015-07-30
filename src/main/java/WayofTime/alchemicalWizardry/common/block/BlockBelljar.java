package WayofTime.alchemicalWizardry.common.block;

import java.util.ArrayList;
import java.util.List;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBelljar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBelljar extends BlockContainer
{
    public BlockBelljar()
    {
        super(Material.glass);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blockCrystalBelljar))
        {
        	par3List.add(new ItemStack(par1, 1, 0));
        	
        	for(Reagent reagent : ReagentRegistry.reagentList.values())
        	{
	        	ItemStack stack = new ItemStack(par1, 1, 0);
	            NBTTagCompound tag = new NBTTagCompound();
	            
	            ReagentContainer[] tanks = new ReagentContainer[1];
	            tanks[0] = new ReagentContainer(reagent, 16000, 16000);
	            
	            NBTTagList tagList = new NBTTagList();
       
	            NBTTagCompound savedTag = new NBTTagCompound();
	            if (tanks[0] != null)
	            {
	                tanks[0].writeToNBT(savedTag);
	            }
	            tagList.appendTag(savedTag);
	            
	
	            tag.setTag("reagentTanks", tagList);
	            
	            stack.setTagCompound(tag);
	            
	            par3List.add(stack);
        	}
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLiving, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(blockPos);

        if (tile instanceof TEBelljar)
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null)
            {
                ((TEBelljar) tile).readTankNBTOnPlace(tag);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEBelljar();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState)
    {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TEBelljar)
        {
            return ((TEBelljar) tile).getRSPowerOutput();
        }
        return 15;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player)
    {
        this.dropBlockAsItem(world, blockPos, blockState, 0);
        super.onBlockHarvested(world, blockPos, blockState, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        TileEntity tile = world.getTileEntity(blockPos);

        if (tile instanceof TEBelljar)
        {
            ItemStack drop = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            ((TEBelljar) tile).writeTankNBT(tag);
            drop.setTagCompound(tag);

            list.add(drop);
        }

        return list;
    }
}
