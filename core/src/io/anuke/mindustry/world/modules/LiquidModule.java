package io.anuke.mindustry.world.modules;

import io.anuke.mindustry.type.Liquid;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LiquidModule extends BlockModule{
    private float[] liquids = new float[Liquid.all().size];
    private float total;
    private Liquid current = Liquid.getByID(0);

    /**
     * Returns total amount of liquids.
     */
    public float total(){
        return total;
    }

    /**
     * Last recieved or loaded liquid. Only valid for liquid modules with 1 type of liquid.
     */
    public Liquid current(){
        return current;
    }

    public float currentAmount(){
        return liquids[current.id];
    }

    public float get(Liquid liquid){
        return liquids[liquid.id];
    }

    public void add(Liquid liquid, float amount){
        liquids[liquid.id] += amount;
        total += amount;
        current = liquid;
    }

    public void remove(Liquid liquid, float amount){
        add(liquid, -amount);
    }

    public void forEach(LiquidConsumer cons){
        for(int i = 0; i < liquids.length; i++){
            if(liquids[i] > 0){
                cons.accept(Liquid.getByID(i), liquids[i]);
            }
        }
    }

    public float sum(LiquidCalculator calc){
        float sum = 0f;
        for(int i = 0; i < liquids.length; i++){
            if(liquids[i] > 0){
                sum += calc.get(Liquid.getByID(i), liquids[i]);
            }
        }
        return sum;
    }

    @Override
    public void write(DataOutput stream) throws IOException{
        byte amount = 0;
        for(float liquid : liquids){
            if(liquid > 0) amount++;
        }

        stream.writeByte(amount); //amount of liquids

        for(int i = 0; i < liquids.length; i++){
            if(liquids[i] > 0){
                stream.writeByte(i); //liquid ID
                stream.writeFloat(liquids[i]); //item amount
            }
        }
    }

    @Override
    public void read(DataInput stream) throws IOException{
        byte count = stream.readByte();

        for(int j = 0; j < count; j++){
            int liquidid = stream.readByte();
            float amount = stream.readFloat();
            liquids[liquidid] = amount;
            if(amount > 0){
                current = Liquid.getByID(liquidid);
            }
            this.total += amount;
        }
    }

    public interface LiquidConsumer{
        void accept(Liquid liquid, float amount);
    }

    public interface LiquidCalculator{
        float get(Liquid liquid, float amount);
    }
}
