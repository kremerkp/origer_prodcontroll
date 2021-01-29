/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

/**
 *
 * @author ffreres
 * @param <T>
 */
public abstract class BasicFormBean<T> extends BasicBean {
    
    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public abstract void save();

    protected abstract void create();
    
    protected abstract void update();
    
    protected abstract void checkInput();

}
