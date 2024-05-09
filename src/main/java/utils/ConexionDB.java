//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {
    protected Connection conexion;
    protected Statement sentencia;
    protected PreparedStatement sentenciaPreparada;
    protected ResultSet resultSet;
    Statement encapsulamiento;


    public ConexionDB(String claseNombre, String cadenaConexion) {
        try {
            Class.forName(claseNombre);
            this.conexion = DriverManager.getConnection(cadenaConexion);
            this.conexion.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException var4) {
            System.out.println(var4.getMessage());
        }

    }

    public ConexionDB(String url, String usuario, String contrasena) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);

            this.encapsulamiento = this.conexion.createStatement();

        } catch (SQLException | ClassNotFoundException var6) {
            System.out.println(var6.getMessage());
        }

    }

    public Statement getSentencia() {
        return this.sentencia;
    }

    public Connection getconexion() {
        return this.conexion;
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public PreparedStatement getSentenciaPreparada() {
        return this.sentenciaPreparada;
    }

    public void commit() {
        try {
            this.conexion.commit();
        } catch (SQLException var2) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public void rollback() {
        try {
            this.conexion.rollback();
        } catch (SQLException var2) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public void cerrarResult() {
        try {
            this.resultSet.close();
        } catch (SQLException var2) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public void cerrarSentencia() {
        try {
            this.sentencia.close();
        } catch (SQLException var2) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public void cerrarConexion() {
        try {
            if (this.resultSet != null) {
                this.cerrarResult();
            }

            if (this.sentencia != null) {
                this.cerrarSentencia();
            }
            this.conexion.close();

        } catch (SQLException var2) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public void ejecutarConsulta(String consulta) throws SQLException {
        Statement sentencia = encapsulamiento;
        this.resultSet = sentencia.executeQuery(consulta);
    }

    public int ejecutarInstruccion(String instruccion) throws SQLException {
        int filas;
        this.sentencia = this.conexion.createStatement();
        filas = this.sentencia.executeUpdate(instruccion);
        return filas;
    }

    public int ejecutarInstruccionCommit(String instruccion, boolean commit) {
        int filas = 0;

        try {
            this.sentencia = this.conexion.createStatement();
            filas = this.sentencia.executeUpdate(instruccion);
            if (commit) {
                this.commit();
            }
        } catch (SQLException var5) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return filas;
    }

    public boolean existeValor(String valor, String columna, String tabla) {
        boolean existe = false;

        try {
            Statement sentenciaAux = this.conexion.createStatement();
            ResultSet aux = sentenciaAux.executeQuery("select count(*) from " + tabla + " where upper(" + columna + ")='" + valor.toUpperCase() + "'");
            aux.next();
            if (aux.getInt(1) >= 1) {
                existe = true;
            }

            aux.close();
            sentenciaAux.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return existe;
    }

    public boolean existeValor(int valor, String tabla, String columna) {
        boolean existe = false;

        try {
            Statement sentenciaAux = this.conexion.createStatement();
            ResultSet aux = sentenciaAux.executeQuery("select count(*) from " + tabla + " where " + columna + "=" + valor + "");
            aux.next();
            if (aux.getInt(1) >= 1) {
                existe = true;
            }

            aux.close();
            sentenciaAux.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return existe;
    }

    public boolean masOIgualQueUno(String query) {
        boolean vacio = false;

        try {
            Statement sentenciaAux = this.conexion.createStatement();
            ResultSet aux = sentenciaAux.executeQuery(query);
            aux.next();
            if (aux.getInt(1) >= 1) {
                vacio = true;
            }

            aux.close();
            sentenciaAux.close();
        } catch (SQLException var5) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return vacio;
    }

    public int devolverValorInt(String columna, String tabla, String condicion) {
        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            int var8;
            try {
                ResultSet aux = sentenciaAux.executeQuery("select " + columna + " from " + tabla + " where " + condicion);
                Throwable var7 = null;

                try {
                    aux.next();
                    var8 = aux.getInt(1);
                } catch (Throwable var33) {

                    var7 = var33;
                    throw var33;
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var32) {
                                var7.addSuppressed(var32);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var5 = var35;
                throw var35;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var31) {
                            var5.addSuppressed(var31);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }

            return (int)var8;
        } catch (SQLException var37) {
            return 0;
        }
    }

    public double devolverValorDouble(String columna, String tabla, String condicion) {
        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            double var8;
            try {
                ResultSet aux = sentenciaAux.executeQuery("select " + columna + " from " + tabla + " where " + condicion);
                Throwable var7 = null;

                try {
                    aux.next();
                    var8 = aux.getDouble(1);
                } catch (Throwable var34) {
                    var7 = var34;
                    throw var34;
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var33) {
                                var7.addSuppressed(var33);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var36) {
                var5 = var36;
                throw var36;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var32) {
                            var5.addSuppressed(var32);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }

            return (double)var8;
        } catch (SQLException var38) {
            return 0.0;
        }
    }

    public String devolverValorString(String columna, String tabla, String condicion) {
        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            Object var8;
            try {
                ResultSet aux = sentenciaAux.executeQuery("select " + columna + " from " + tabla + " where " + condicion);
                Throwable var7 = null;

                try {
                    if (!this.consultaVacia("select " + columna + " from " + tabla + " where " + condicion)) {
                        aux.next();
                        var8 = aux.getString(1);
                        return (String)var8;
                    }

                    JOptionPane.showMessageDialog((Component)null, "Error, consulta vacia");
                    var8 = null;
                } catch (Throwable var36) {
                    var8 = var36;
                    var7 = var36;
                    throw var36;
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var35) {
                                var7.addSuppressed(var35);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var38) {
                var5 = var38;
                throw var38;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var34) {
                            var5.addSuppressed(var34);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }

            return (String)var8;
        } catch (SQLException var40) {
            return null;
        }
    }

    public int[] devolverValoresInt(String columna, String tabla, String condicion) {
        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            int total;
            try {
                ResultSet aux = sentenciaAux.executeQuery("select " + columna + " from " + tabla + " where " + condicion);
                Throwable var7 = null;

                try {
                    if (!this.consultaVacia("select count(" + columna + ") from " + tabla + " where " + condicion)) {
                        total = this.cuentaRegistrosConsulta(tabla, condicion);
                        int[] valores = new int[total];

                        for(int i = 0; aux.next(); ++i) {
                            valores[i] = aux.getInt(1);
                        }

                        int[] var43 = valores;
                        return var43;
                    }

                    JOptionPane.showMessageDialog((Component)null, "Error, consulta vacia");
                    total = Integer.parseInt(null);
                } catch (Throwable var38) {
                    var7 = var38;
                    throw var38;
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var37) {
                                var7.addSuppressed(var37);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var40) {
                var5 = var40;
                throw var40;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var36) {
                            var5.addSuppressed(var36);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }

            int[] array = new int[1];
            array[0] = total;
            return array;
        } catch (SQLException var42) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var42);
            return null;
        }
    }

    public String[] devolverValoresString(String columna, String tabla, String condicion) {
        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            try {
                ResultSet aux = sentenciaAux.executeQuery("select " + columna + " from " + tabla + " where " + condicion);
                Throwable var7 = null;

                try {
                    int total;
                    try {
                        if (this.consultaVacia("select count(" + columna + ") from " + tabla + " where " + condicion)) {
                            JOptionPane.showMessageDialog((Component)null, "Error, consulta vacia");
                            total = Integer.parseInt(null);
                            return new String[]{String.valueOf(total)};
                        } else {
                            total = this.cuentaRegistrosConsulta(tabla, condicion);
                            String[] valores = new String[total];

                            for(int i = 0; aux.next(); ++i) {
                                valores[i] = aux.getString(1);
                            }

                            String[] var43 = valores;
                            return var43;
                        }
                    } catch (Throwable var38) {
                        var7 = var38;
                        throw var38;
                    }
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var37) {
                                var7.addSuppressed(var37);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var40) {
                var5 = var40;
                throw var40;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var36) {
                            var5.addSuppressed(var36);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }
        } catch (SQLException var42) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var42);
            return null;
        }
    }

    public int cuentaRegistrosConsulta(String tabla, String condicion) {
        String consulta;
        if (condicion.equals("")) {
            consulta = "select count(*) from " + tabla;
        } else {
            consulta = "select count(*) from " + tabla + " where " + condicion;
        }

        try {
            Statement sentenciaAux = this.conexion.createStatement();
            Throwable var5 = null;

            int var8;
            try {
                ResultSet aux = sentenciaAux.executeQuery(consulta);
                Throwable var7 = null;

                try {
                    var8 = aux.getInt(0);
                } catch (Throwable var33) {
                    var7 = var33;
                    throw var33;
                } finally {
                    if (aux != null) {
                        if (var7 != null) {
                            try {
                                aux.close();
                            } catch (Throwable var32) {
                                var7.addSuppressed(var32);
                            }
                        } else {
                            aux.close();
                        }
                    }

                }
            } catch (Throwable var35) {
                var5 = var35;
                throw var35;
            } finally {
                if (sentenciaAux != null) {
                    if (var5 != null) {
                        try {
                            sentenciaAux.close();
                        } catch (Throwable var31) {
                            var5.addSuppressed(var31);
                        }
                    } else {
                        sentenciaAux.close();
                    }
                }

            }

            return (int)var8;
        } catch (SQLException var37) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var37);
            return -1;
        }
    }

    public boolean consultaVacia(String query) {
        boolean vacio = false;

        try {
            Statement sentenciaAux = this.conexion.createStatement();
            ResultSet aux = sentenciaAux.executeQuery(query);
            aux.next();
            if (aux.getInt(1) == 0) {
                vacio = true;
            }

            aux.close();
            sentenciaAux.close();
        } catch (SQLException var5) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return vacio;
    }

    public int ultimoID(String columnaID, String tabla) throws SQLException {
        int IDMaximo;
        Statement sm = this.conexion.createStatement();
        ResultSet rs = sm.executeQuery("select max(" + columnaID + ") as " + columnaID + " from " + tabla + "");
        rs.next();
        IDMaximo = rs.getInt(columnaID);
        rs.close();
        sm.close();
        return IDMaximo;
    }

    public int proximoIDDisponible(String columnaID, String tabla) throws SQLException {
        int id = this.ultimoID(columnaID, tabla);
        return id == -1 ? 1 : id + 1;
    }

    public int ultimoIDSinEliminar(String columnaEliminado, String columnaID, String tabla) {
        int IDMaximo = -1;

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs = sm.executeQuery("select max(" + columnaID + ") as " + columnaID + " from " + tabla + " where " + columnaEliminado + "=0");
            rs.next();
            IDMaximo = rs.getInt(columnaID);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return IDMaximo;
    }

    public int primerID(String columnaID, String tabla) {
        int IDMaximo = -1;

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs = sm.executeQuery("select min(" + columnaID + ") as " + columnaID + " from " + tabla + "");
            rs.next();
            IDMaximo = rs.getInt(columnaID);
            rs.close();
            sm.close();
        } catch (SQLException var6) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return IDMaximo;
    }

    public int primerIDSinEliminar(String columnaEliminado, String columnaID, String tabla) {
        int IDMaximo = -1;

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs = sm.executeQuery("select min(" + columnaID + ") as " + columnaID + " from " + tabla + " where " + columnaEliminado + "=0");
            rs.next();
            IDMaximo = rs.getInt(columnaID);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return IDMaximo;
    }

    public String minimoDe(String columna, String tabla, String condicion) {
        String resultado = "";

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs;
            if (condicion.equals("")) {
                rs = sm.executeQuery("select min(" + columna + ") as " + columna + " from " + tabla + "");
            } else {
                rs = sm.executeQuery("select min(" + columna + ") as " + columna + " from " + tabla + " where " + condicion);
            }

            rs.next();
            resultado = rs.getString(columna);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return resultado;
    }

    public String maximoDe(String columna, String tabla, String condicion) {
        String resultado = "";

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs;
            if (condicion.equals("")) {
                rs = sm.executeQuery("select max(" + columna + ") as " + columna + " from " + tabla + "");
            } else {
                rs = sm.executeQuery("select max(" + columna + ") as " + columna + " from " + tabla + " where " + condicion);
            }

            rs.next();
            resultado = rs.getString(columna);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return resultado;
    }

    public int sumaDeInt(String columna, String tabla, String condicion) {
        String resultado = "";

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs;
            if (condicion.equals("")) {
                rs = sm.executeQuery("select sum(" + columna + ") as " + columna + " from " + tabla + "");
            } else {
                rs = sm.executeQuery("select sum(" + columna + ") as " + columna + " from " + tabla + " where " + condicion);
            }

            rs.next();
            resultado = rs.getString(columna);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return Integer.parseInt(resultado);
    }

    public double sumaDeDouble(String columna, String tabla, String condicion) {
        String resultado = "";

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet rs;
            if (condicion.equals("")) {
                rs = sm.executeQuery("select sum(" + columna + ") as " + columna + " from " + tabla + "");
            } else {
                rs = sm.executeQuery("select sum(" + columna + ") as " + columna + " from " + tabla + " where " + condicion);
            }

            rs.next();
            resultado = rs.getString(columna);
            rs.close();
            sm.close();
        } catch (SQLException var7) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return Double.parseDouble(resultado);
    }

    public void rellenaComboBoxBDString(JComboBox cmb, String columna, String tabla, String condicion) {
        cmb.removeAllItems();

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet consulta = sm.executeQuery("select distinct " + columna + " from " + tabla);
            ResultSet correspondiente = null;
            if (condicion.equals("")) {
                while(consulta.next()) {
                    cmb.addItem(consulta.getString(columna));
                }
            } else {
                Statement smAux = this.conexion.createStatement();
                correspondiente = sm.executeQuery("select distinct " + columna + " from " + tabla + " where " + condicion);
                correspondiente.next();

                while(consulta.next()) {
                    cmb.addItem(consulta.getString(columna));
                    if (correspondiente.getString(columna).equals(consulta.getString(columna))) {
                        cmb.setSelectedItem(correspondiente.getString(columna));
                    }
                }

                correspondiente.close();
                smAux.close();
            }

            consulta.close();
            sm.close();
        } catch (SQLException var9) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var9);
        }

    }

    public void rellenaComboBoxBDInt(JComboBox cmb, String tabla, String columna, String condicion) {
        cmb.removeAllItems();

        try {
            Statement sm = this.conexion.createStatement();
            ResultSet consulta = sm.executeQuery("select distinct " + columna + " from " + tabla);
            ResultSet correspondiente = null;
            if (condicion.equals("")) {
                while(consulta.next()) {
                    cmb.addItem(consulta.getInt(columna));
                }
            } else {
                Statement smAux = this.conexion.createStatement();
                correspondiente = smAux.executeQuery("select distinct " + columna + " from " + tabla + " where " + condicion);
                correspondiente.next();

                while(consulta.next()) {
                    cmb.addItem(consulta.getInt(columna));
                    if (correspondiente.getInt(columna) == consulta.getInt(columna)) {
                        cmb.setSelectedItem(correspondiente.getInt(columna));
                    }
                }

                correspondiente.close();
                smAux.close();
            }

            consulta.close();
            sm.close();
        } catch (SQLException var9) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, (String)null, var9);
        }

    }

    public void rellenaComboBox2Columnas(JComboBox cmb, String consulta, String inicio, String columnaNoVisible, String columnaVisible) {
        String[] datos = new String[2];

        try {
            cmb.removeAllItems();
            Statement aux = this.conexion.createStatement();
            ResultSet resultado = aux.executeQuery(consulta);
            if (!inicio.equals("")) {
                datos[0] = Integer.toString(-1);
                datos[1] = inicio;
                cmb.addItem(new String[]{datos[0], datos[1]});
            }

            while(resultado.next()) {
                datos[0] = Integer.toString(resultado.getInt(columnaNoVisible));
                datos[1] = resultado.getString(columnaVisible);
                cmb.addItem(new String[]{datos[0], datos[1]});
            }

            cmb.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList l, Object o, int i, boolean s, boolean f) {
                    return new JLabel(((String[])((String[])o))[1]);
                }
            });
        } catch (SQLException var9) {
            System.out.println(var9.getStackTrace());
        }

    }

    public void rellenaJTableBD(DefaultTableModel tabla) throws SQLException {
        ResultSetMetaData metadatos = this.resultSet.getMetaData();
        tabla.setColumnCount(metadatos.getColumnCount());
        int numeroColumnas = tabla.getColumnCount();
        String[] etiquetas = new String[numeroColumnas];

        for(int i = 0; i < numeroColumnas; ++i) {
            etiquetas[i] = metadatos.getColumnLabel(i + 1);
        }

        tabla.setColumnIdentifiers(etiquetas);

        while(this.resultSet.next()) {
            Object[] datosFila = new Object[tabla.getColumnCount()];

            for(int i = 0; i < tabla.getColumnCount(); ++i) {
                datosFila[i] = this.resultSet.getObject(i + 1);
            }

            tabla.addRow(datosFila);
        }

        this.cerrarResult();
    }

    public void ejecutarConsultaPreparada(String consulta, Object[] valores) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        this.rellenarSentenciaPreparada(valores);
        this.resultSet = this.sentenciaPreparada.executeQuery();
    }

    public void ejecutarConsultaPreparada(String consulta, ArrayList valores) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        this.rellenarSentenciaPreparada(valores);
        this.resultSet = this.sentenciaPreparada.executeQuery();
    }

    public void ejecutarConsultaPreparada(String consulta) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        this.resultSet = this.sentenciaPreparada.executeQuery();
    }

    public int ejecutarInstruccionPreparada(String consulta, Object[] valores) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        this.rellenarSentenciaPreparada(valores);
        int filas = this.sentenciaPreparada.executeUpdate();
        return filas;
    }

    public int ejecutarInstruccionPreparada(String consulta, ArrayList valores) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        this.rellenarSentenciaPreparada(valores);
        int filas = this.sentenciaPreparada.executeUpdate();
        return filas;
    }

    public int ejecutarInstruccionPreparada(String consulta) throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
        int filas = this.sentenciaPreparada.executeUpdate();
        return filas;
    }

    private void rellenarSentenciaPreparada(Object[] valores) throws SQLException {
        if (valores != null && valores.length > 0) {
            for(int i = 0; i < valores.length; ++i) {
                Object obj = valores[i];
                int indice = i + 1;
                if (obj instanceof Integer) {
                    this.sentenciaPreparada.setInt(indice, (Integer)obj);
                } else if (obj instanceof String) {
                    this.sentenciaPreparada.setString(indice, (String)obj);
                } else if (obj instanceof Double) {
                    this.sentenciaPreparada.setDouble(indice, (Double)obj);
                }
            }
        }

    }

    private void rellenarSentenciaPreparada(ArrayList valores) throws SQLException {
        if (valores != null && valores.size() > 0) {
            int indice = 1;

            for(Iterator var3 = valores.iterator(); var3.hasNext(); ++indice) {
                Object obj = var3.next();
                if (obj instanceof Integer) {
                    this.sentenciaPreparada.setInt(indice, (Integer)obj);
                } else if (obj instanceof String) {
                    this.sentenciaPreparada.setString(indice, (String)obj);
                } else if (obj instanceof Double) {
                    this.sentenciaPreparada.setDouble(indice, (Double)obj);
                }
            }
        }

    }
}
