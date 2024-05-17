package net.cltech.enterprisent.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Clase que permite la configuracion entre la capa de job factory
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/12/2018
 * @see Creacion
 */
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware
{

    private transient AutowireCapableBeanFactory beanFactory;

    /**
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     *
     * @param bundle
     * @return
     * @throws Exception
     */
    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception
    {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
