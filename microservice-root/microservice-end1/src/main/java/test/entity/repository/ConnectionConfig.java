package test.entity.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;

public class ConnectionConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return null;
	}

	@Override
	public Mongo mongo() throws Exception {
		return null;
	}
}
