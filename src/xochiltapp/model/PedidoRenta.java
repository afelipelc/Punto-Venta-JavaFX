/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import xochiltapp.datasource.ClientesDataSource;

public class PedidoRenta {

    private long idPedidoRenta;
    private Cliente cliente;
    private String nombreCliente;
    private int idCliente;
    private Date fechaSolicitud;
    private Date fechaEntrega;
    private String fechaSolicitudString;
    private String fechaEntregaString;
    private Date fechaEntregado;
    private Date fechaEnviado;
    private String direccionCliente;
    private String direccionEntrega;
    private String nombreRecibe;
    private boolean pedido;
    private boolean renta;
    private float importe;
    private float abono;
    private String empleado;
    private String nota = "";
    private boolean pagado = false;
    private boolean enviado = false;
    private boolean entregado = false;
    private boolean devuelto = false;
    private boolean cancelado = false;
    private List<DetallePedidoRenta> detallesPedidoRenta = new ArrayList<>();
    private List<Long> detallesEliminar = new ArrayList<>();
    private String estado;
    private String tipo;

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    
    /**
     * @return the detallesPedidoRenta
     */
    public List<DetallePedidoRenta> getDetallesPedidoRenta() {
        return detallesPedidoRenta;
    }

    /**
     * @param detallesPedidoRenta the detallesPedidoRenta to set
     */
    public void setDetallesPedidoRenta(List<DetallePedidoRenta> detallesPedidoRenta) {
        this.detallesPedidoRenta = detallesPedidoRenta;
    }

    /**
     * @return the idPedidoRenta
     */
    public long getIdPedidoRenta() {
        return idPedidoRenta;
    }

    /**
     * @param idPedidoRenta the idPedidoRenta to set
     */
    public void setIdPedidoRenta(long idPedidoRenta) {
        this.idPedidoRenta = idPedidoRenta;
    }

    /**
     * @return the fechaSolicitud
     */
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    /**
     * @param fechaSolicitud the fechaSolicitud to set
     */
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    /**
     * @return the fechaEntrega
     */
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the fechaEntregado
     */
    public Date getFechaEntregado() {
        return fechaEntregado;
    }

    /**
     * @param fechaEntregado the fechaEntregado to set
     */
    public void setFechaEntregado(Date fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    /**
     * @return the importe
     */
    public float getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(float importe) {
        this.importe = importe;
    }

    /**
     * @return the empleado
     */
    public String getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public void addDetalle(DetallePedidoRenta detalle) {
        boolean existe = false;
        for (DetallePedidoRenta item : this.detallesPedidoRenta) {
            if (item.getIdProducto() == detalle.getIdProducto()) {
                //agregar
                item.setCantidad(item.getCantidad() + detalle.getCantidad());
                existe = true;
                break;

            }
        }
        if (!existe) {
            this.detallesPedidoRenta.add(detalle);
        }
    }

    /**
     * @return the pedido
     */
    public boolean isPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(boolean pedido) {
        this.pedido = pedido;
        this.renta = !pedido;
    }

    /**
     * @return the renta
     */
    public boolean isRenta() {
        return renta;
    }

    /**
     * @param renta the renta to set
     */
    public void setRenta(boolean renta) {
        this.renta = renta;
        this.pedido = !renta;
    }

    /**
     * @return the direccionCliente
     */
    public String getDireccionCliente() {
        return direccionCliente;
    }

    /**
     * @param direccionCliente the direccionCliente to set
     */
    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    /**
     * @return the direccionEntrega
     */
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    /**
     * @param direccionEntrega the direccionEntrega to set
     */
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    /**
     * @return the nombreRecibe
     */
    public String getNombreRecibe() {
        return nombreRecibe;
    }

    /**
     * @param nombreRecibe the nombreRecibe to set
     */
    public void setNombreRecibe(String nombreRecibe) {
        this.nombreRecibe = nombreRecibe;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        if (cliente != null) {
            return cliente;
        } else if (idCliente > 0) {
            cliente = ClientesDataSource.findClienteById(idCliente);
            return cliente;
        } else {
            return null;
        }
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @return the abono
     */
    public float getAbono() {
        return abono;
    }

    /**
     * @param abono the abono to set
     */
    public void setAbono(float abono) {
        this.abono = abono;
    }

    /**
     * @return the pagado
     */
    public boolean isPagado() {
        return pagado;
    }

    /**
     * @param pagado the pagado to set
     */
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    /**
     * @return the enviado
     */
    public boolean isEnviado() {
        return enviado;
    }

    /**
     * @param enviado the enviado to set
     */
    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    /**
     * @return the entregado
     */
    public boolean isEntregado() {
        return entregado;
    }

    /**
     * @param entregado the entregado to set
     */
    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    /**
     * @return the devuelto
     */
    public boolean isDevuelto() {
        return devuelto;
    }

    /**
     * @param devuelto the devuelto to set
     */
    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    /**
     * @return the cancelado
     */
    public boolean isCancelado() {
        return cancelado;
    }

    /**
     * @param cancelado the cancelado to set
     */
    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        if (cliente != null) {
            return cliente.getIdCliente();
        } else {
            return idCliente;
        }
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void addDetalleEliminar(long idDetalle) {
        if (idDetalle > 0) {
            this.detallesEliminar.add(idDetalle);
        }
    }

    public List<Long> getDetallesEliminar() {
        return this.detallesEliminar;
    }

    /**
     * @return the nombreCliente
     */
    public String getNombreCliente() {
        if (cliente != null && nombreCliente==null) {
            nombreCliente = cliente.getNombre();
        } else if (idCliente > 0 && nombreCliente==null) {
            cliente = ClientesDataSource.findClienteById(idCliente);
            nombreCliente = cliente.getNombre();
        }
        return nombreCliente;
    }

    /**
     * @param nombreCliente the nombreCliente to set
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        
        if(enviado)
            estado="Enviado";
        else if(entregado)
            estado="Entregado";
        else
            estado="Esperando fecha";
        
        return estado;
    }


    /**
     * @return the tipo
     */
    public String getTipo() {
        if(pedido)
            tipo="Pedido";
        if(renta)
            tipo="Renta";
        
        return tipo;
    }


    /**
     * @return the fechaSolicitudString
     */
    public String getFechaSolicitudString() {
        if(fechaSolicitudString == null && fechaSolicitud !=null)
        fechaSolicitudString = dateFormat.format(fechaSolicitud);
        return fechaSolicitudString;
    }

    /**
     * @return the fechaEntregaString
     */
    public String getFechaEntregaString() {
                if(fechaEntregaString == null && fechaEntrega !=null)
        fechaEntregaString = dateFormat.format(fechaEntrega);

        return fechaEntregaString;
    }

    /**
     * @return the fechaEnviado
     */
    public Date getFechaEnviado() {
        return fechaEnviado;
    }

    /**
     * @param fechaEnviado the fechaEnviado to set
     */
    public void setFechaEnviado(Date fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }

}
