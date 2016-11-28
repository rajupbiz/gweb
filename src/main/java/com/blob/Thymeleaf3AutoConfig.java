package com.blob;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
//@ConditionalOnClass({SpringTemplateEngine.class})
//@EnableConfigurationProperties({ThymeleafProperties.class})  //no sense rolling our own.
//@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class Thymeleaf3AutoConfig { // implements ApplicationContextAware {

    //private ApplicationContext applicationContext;

    /*@Autowired
    private ThymeleafProperties properties;
	*/
   /* public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }*/
	
    @Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

  /*  @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setOrder(2147483642);
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }*/

    /*@Bean
    //made this @Bean (vs private in Thymeleaf migration docs ), otherwise MessageSource wasn't autowired.
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        Set<IDialect> dialects = new HashSet<>();
        //dialects.add( new LayoutDialect());	
        //dialects.add(new SpringSecurityDialect());
        engine.setAdditionalDialects(dialects );
        
        
        return engine;
    }*/

    /*private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(this.properties.getPrefix());
        resolver.setSuffix(this.properties.getSuffix());
        resolver.setTemplateMode(this.properties.getMode());
        resolver.setCacheable(this.properties.isCache());
        return resolver;
    }*/


}