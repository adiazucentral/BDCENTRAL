package net.cltech.securitynt.tools.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Listados de la aplicación
 *
 * @author Eacuna
 * @version 1.0.0
 * @since 26/10/2017
 * @see Creación
 */
public class ListEnum
{

    /**
     * Id´s del genero
     */
    public enum Gender
    {
        MALE(7),
        FEMALE(8),
        UNDEFINED(9),
        BOTH(42);

        private final int value;

        Gender(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public boolean isValid(int gender)
        {
            List<Integer> genders = Arrays.asList(MALE.getValue(), FEMALE.getValue(), BOTH.getValue(), UNDEFINED.getValue());
            return genders.contains(gender);
        }
    }

    /**
     * Motivos
     */
    public enum Reason
    {
        ORDER_EDIT(15),
        ORDER_DELETE(16);

        private final int value;

        Reason(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Id´s del tipo de destino
     */
    public enum DestinationType
    {
        INITIAL(44),
        INTERNALPROCESS(45),
        EXTERNALPROCESS(46),
        CONTROL(47),
        FINAL(48);

        private final int value;

        DestinationType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Tipos de entrega
     */
    public enum DeliveryType
    {
        PRINT(59),
        EMAIL(60),
        WEB(61),
        APP(62);

        private final int value;

        DeliveryType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

}
