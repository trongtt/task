package org.exoplatform.task.management.model;

import org.json.simple.JSONObject;

public class ViewState extends JSONObject {
  public ViewState(JSONObject json) {
    super(json);
  }

  public String getProjectId() {
    return get("projectId").toString();
  }

  private String getFilter() {
    return get("filter").toString();
  }

  public String getLabelId() {
    return get("labelId").toString();
  }

  public String getId() {
    return buildId(getProjectId(), getFilter(), getLabelId());
  }

  public static String buildId(String projectId, String filter, String labelId) {
    StringBuilder sBuilder = new StringBuilder("p#").append(projectId);

    if (filter != null) {
      sBuilder.append(".f#").append(filter);
    }

    if (labelId != null) {
      sBuilder.append(".l#").append(labelId);
    }

    return sBuilder.toString();
  }
}
