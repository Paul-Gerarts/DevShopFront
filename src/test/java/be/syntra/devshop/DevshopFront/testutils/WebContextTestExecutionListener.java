package be.syntra.devshop.DevshopFront.testutils;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.web.context.WebApplicationContext;

public class WebContextTestExecutionListener implements TestExecutionListener {
    @Override
    public void prepareTestInstance(TestContext testContext) {
        if (testContext.getApplicationContext() instanceof GenericApplicationContext) {
            GenericApplicationContext context = (GenericApplicationContext) testContext.getApplicationContext();
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            /*beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST,
                    new SimpleThreadScope());*/
            beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION,
                    new SimpleThreadScope());
        }
    }
}
