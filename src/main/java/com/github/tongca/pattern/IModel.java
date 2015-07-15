package com.github.tongca.pattern;

import java.util.Date;

public interface IModel<ID> {
	ID getId();

	Date getModifiedDate();
}
