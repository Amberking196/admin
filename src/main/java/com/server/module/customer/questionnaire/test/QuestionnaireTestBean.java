package com.server.module.customer.questionnaire.test;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.customer.questionnaire.QuestionnaireUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * table name: questionnaire_test author name: yjr create time: 2019-01-25
 * 17:02:19
 */
@Data
@Entity(tableName = "questionnaire_test", id = "id", idGenerate = "auto")
public class QuestionnaireTestBean {

	private Long id;
	private String title;
	private String remark;
	private String types;
	@NotField
	private String typesLabel;
	private Integer state;
	private String smsContent;
	private Date createTime;
	private Long createUserId;
	private Long updateUserId;
	private Date updateTime;
	private Long companyId;
	@NotField
	private String companyName;

	public String getTypesLabel() {
		typesLabel = "";
		if (types != null && !types.equals("")) {
			String type[] = types.split(",");

			boolean first = true;
			for (String s : type) {
				String label = QuestionnaireUtils.userTypeMap.get(Integer.parseInt(s));
				if (first)
					typesLabel = label;
				else
					typesLabel = typesLabel + "," + label;
				first = false;
			}
		}
		return typesLabel;
	}

}
