package lol.spells;

public enum Spell{
    CLEANSE(1),
    EXHAUST(3),
    FLASH(4),
    GHOST(6),
    HEAL(7),
    SMITE(11),
    TELEPORT(12),
    CLARITY(13),
    IGNITE(14),
    BARRIER(21),
    TO_THE_KING(30),
    PORO_TOSS(31),
    SNOWBALL(32),
    URF_SNOWBALL(39);

    public final int id;
    Spell(int id){
        this.id = id;
    }
    public static Spell from(long id){
        Spell[] spells = values();
        for (Spell spell : spells){
            if (spell.id == id){
                return spell;
            }
        }
        throw new IllegalArgumentException("Illegal spell id: " + id);
    }
}
