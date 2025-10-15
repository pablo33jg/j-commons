package com.pj.commons.utils;

public class QCommons {

    public static final Integer TIPO_TRANSACCION_COMPRAS = 7;
    public static final Integer TIPO_TRANSACCION_DEVOLUCION_COMPRA = 8;
    public static final Integer TIPO_TRANSACCION_NOTAS_CREDITO_COMPRA = 10;
    public static final Integer TIPO_TRANSACCION_VENTAS = 13;
    public static final Integer TIPO_TRANSACCION_DEVOLUCIONES_VENTAS = 14;
    public static final Integer TIPO_TRANSACCION_NOTAS_CREDITO_VENTAS = 16;
    public static final Integer TIPO_TRANSACCION_TRASPASOS = 3;
    public static final Integer TIPO_TRANSACCION_RECEPCION_TRASPASOS = 4;
    public static final Integer TIPO_TRANSACCION_CUENTA_PAGAR = 17;
    public static final Integer TIPO_TRANSACCION_CUENTA_COBRAR = 18;
    public static final Integer TIPO_TRANSACCION_REEMBOLSO_CLIENTE = 20;
    public static final Integer TIPO_TRANSACCION_REEMBOLSO_PROVEEDOR = 19;

    public static final Integer TIPO_ESTADO_TRANSACCION_ACTIVO = 0;
    public static final Integer TIPO_ESTADO_TRANSACCION_CANCELADO = 2;
    public static final Integer TIPO_ESTADO_TRANSACCION_PROCESO = 3;

    public static final String TIPO_ESTADO_ACTIVO = "ACTIVO";
    public static final String TIPO_ESTADO_CANCELADO = "CANCELADO";

    public static final Integer TIPO_DOCUMENTO_NORMAL_ID = 0;
    public static final Integer TIPO_DOCUMENTO_DIRECTO_ID = 5;
    public static final String TIPO_DOCUMENTO_NORMAL = "NORMAL";
    public static final String TIPO_DOCUMENTO_DIRECTO = "DIRECTO";

    public static final Integer TIPO_SURTIDO_ABIERTO_ID = 0;
    public static final Integer TIPO_SURTIDO_CERRADO_ID = 2;
    public static final String TIPO_SURTIDO_ABIERTO = "ABIERTA";
    public static final String TIPO_SURTIDO_CERRADO = "CERRADA";

    public static final Integer TIPO_ENTIDAD_CLIENTE = 2;
    public static final Integer TIPO_ENTIDAD_PROVEEDOR = 1;

    public static final Integer CONCEPTO_VENTAS = 12;
    public static final Integer CONCEPTO_COMPRAS = 3;

    public static final Integer TIPO_MODULO_VENTAS = 5;
    public static final Integer TIPO_MODULO_COMPRAS = 2;
    public static final Integer TIPO_MODULO_POLIZAS = 9;
    public static final Integer TIPO_MODULO_REEMBOLSO_PAGOS = 3;

    public static final Integer TIPO_DOCUMENTO_VENTAS = 8;
    public static final Integer TIPO_DOCUMENTO_COMPRAS = 0;
    public static final Integer TIPO_CONCILIACION_VENTAS = 3;
    public static final Integer TIPO_CONCILIACION_COMPRAS = 0;
    public static final Integer TIPO_CUENTA_VENTAS = 2;
    public static final Integer TIPO_CUENTA_COMPRAS = 1;

    public static final Integer CONCEPTO_DEVOLUCIONES_VENTAS = 13;
    public static final Integer TIPO_MODULO_DEVOLUCIONES_VENTAS = 10;

    public static final Integer CONCEPTO_NOTAS_CREDITO_VENTAS = 15;
    public static final Integer TIPO_MODULO_NOTAS_CREDITO_VENTAS = 5;

    public static final Integer CONCEPTO_TRASPASO = 21;
    public static final Integer CONCEPTO_RECEPCION_TRASPASO = 17;
    public static final Integer CONCEPTO_REEMBOLSO_CLIENTE = 25;
    public static final Integer CONCEPTO_REEMBOLSO_PROVEEDOR = 26;

    public static final Integer TIPO_PAGO_CONTADO = 0;
    public static final Integer TIPO_PAGO_CREDITO = 1;
    public static final Integer MEDIO_ENVIO_NOSOTROS_MISMOS = 1;

    public static final Double TIPO_CAMBIO_MXN = 1d;
    public static final Integer TIPO_ALMACEN_TRANSITO = 1;

    public static final Integer CONCEPTO_CUENTA_PAGAR = 23;
    public static final Integer CONCEPTO_CUENTA_COBRAR = 24;

    public static final Integer ACCESO_ALMACENES_GENERAL = 1;
    public static final Integer ACCESO_ALMACENES_GERENTE_REGIONAL = 2;
    public static final Integer ACCESO_ALMACENES_GERENTE_ALMACE = 3;

    public static final Integer ACCESO_COMPRAS_GENERAL = 1;
    public static final Integer ACCESO_COMPRAS_PROVEEDOR = 2;

    public static final Integer ACCESO_VENTAS_ALL = 1;
    public static final Integer ACCESO_VENTAS_GERENTE_REGIONAL = 2;
    public static final Integer ACCESO_VENTAS_GERENTE_ALMACEN = 3;
    public static final Integer ACCESO_VENTAS_VENDEDOR = 4;

    public static final Integer FORMA_PAGO_DEFAULT_ID = 1;
}
