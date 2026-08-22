package com.mrbysco.spawnoverlay.optimizer;

import com.mrbysco.spawnoverlay.optimizer.rule.GhastRule;
import com.mrbysco.spawnoverlay.optimizer.rule.SpawnRule;
import com.mrbysco.spawnoverlay.optimizer.rule.SpiderRule;

public enum OptimizerType {
	SPIDER(new SpiderRule()),
	GHAST(new GhastRule());

	private final SpawnRule rule;

	OptimizerType(SpawnRule rule) {
		this.rule = rule;
	}

	public SpawnRule getRule() {
		return rule;
	}
}
