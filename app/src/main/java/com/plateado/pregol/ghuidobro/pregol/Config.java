package com.plateado.pregol.ghuidobro.pregol;

/**
 * Created by Belal on 10/24/2015.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://67.23.251.165/~jarol/new/addEmp.php";
    public static final String URL_GET_ALL = "http://67.23.251.165/~jarol/new/getAllEmp.php";
    public static final String URL_GET_EMP = "http://67.23.251.165/~jarol/new/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://67.23.251.165/~jarol/new/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://67.23.251.165/~jarol/new/deleteEmp.php?id=";

    public static final String URL_FIX = "http://67.23.251.165/~jarol/fixture.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "desg";
    public static final String KEY_EMP_SAL = "salary";

    public static final String KEY_FIX_PARTIDO = "partido";
    public static final String KEY_FIX_RESULTADO = "resultado";
    public static final String KEY_FIX_FECHA = "fecha";
    public static final String KEY_FIX_USUARIO = "usuario";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="fixture";
    public static final String TAG_ID = "id_partido";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESG = "desg";
    public static final String TAG_SAL = "salary";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}
