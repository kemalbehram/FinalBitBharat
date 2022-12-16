package com.mobiloitte.order.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

	@Autowired
	private AppConfig appConfig;

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		name = super.toPhysicalTableName(name, jdbcEnvironment);
		if (name.getText().contains("seq"))
			return name;
		else
			return new Identifier(String.join("_", name.getText(), appConfig.getInstrument()), name.isQuoted());
	}
}
