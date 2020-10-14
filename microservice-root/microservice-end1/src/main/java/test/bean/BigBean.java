package test.bean;

import java.util.Map;

import test.entity.PrimaryEntity;

public class BigBean {
	private Map<String, PrimaryEntity> map;

	public Map<String, PrimaryEntity> getMap() {
		return map;
	}

	public void setMap(Map<String, PrimaryEntity> map) {
		this.map = map;
	}
}
