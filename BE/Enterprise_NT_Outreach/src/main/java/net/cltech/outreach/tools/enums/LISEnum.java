package net.cltech.outreach.tools.enums;

/**
 * Tipos y estados de la aplicación
 *
 * @author Jblanco
 * @version 1.0.0
 * @since 20/10/2017
 * @see Creación
 */
public class LISEnum
{

    /**
     * Estados del exámen
     */
    public enum ResultTestState
    {
        ORDERED(0),
        RERUN(1),
        REPORTED(2),
        PREVIEW(3),
        VALIDATED(4),
        PRINTED(5);

        private final int value;

        ResultTestState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Estados de la muestra
     */
    public enum ResultSampleState
    {
        REJECTED(0),
        NEW_SAMPLE(1),
        ORDERED(2),
        COLLECTED(3),
        CHECKED(4);  //La muestra llega al laboratorio

        private final int value;

        ResultSampleState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Estados del resultado
     */
    public enum ResultTestPathology
    {
        NORMAL(0),
        PATOLOGY(1),
        LOW_REFERENCE(2),
        HIGH_REFERENCE(3),
        PANIC(4),
        LOW_PANIC(5),
        HIGH_PANIC(6),
        CRITICAL(7),
        LOW_CRITICAL(8),
        HIGH_CRITICAL(9);

        private final int value;

        ResultTestPathology(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Estados de la orden
     */
    public enum ResultOrderState
    {
        CANCELED(0),
        ORDERED(1),
        INPROCESS(2),
        COMPLETED(3);

        private final int value;

        ResultOrderState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Tipos de resultado
     */
    public enum ResultTestResultType
    {
        NUMERIC(1),
        TEXT(2);

        private final int value;

        ResultTestResultType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Unificacion de estados del examen y la muestra
     */
    public enum ResultTestCommonState
    {
        ORDERED(0), TAKED(1), CHECKED(2), REPORTED(3), VALIDATED(4), PRINTED(5),
        TEXT(2);

        private final int value;

        ResultTestCommonState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Tipos de Modificacion.
     */
    public enum ResultReason
    {
        MODIFY("M"), REPEAT("R");

        private final String value;

        ResultReason(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    public enum OrderOrigin
    {
        RECORD("H"), ORDER("O");

        private final String value;

        OrderOrigin(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

}
