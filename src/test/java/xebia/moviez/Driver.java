package xebia.moviez;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import xebia.moviez.conf.Config;

public class Driver {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
		ctx.scan("xebia.moviez");
		ctx.refresh();
		
	}
	
	

}
