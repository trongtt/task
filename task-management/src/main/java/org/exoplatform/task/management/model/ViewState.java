package org.exoplatform.task.management.model;

import org.exoplatform.task.domain.Priority;
import org.exoplatform.task.util.TaskUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class ViewState extends JSONObject {

  private Filter filter;

  public ViewState() {
    filter = new Filter();
  }

  public ViewState(JSONObject json) {
    super(json);

    filter = new Filter((JSONObject) json.get("filter"));
  }

  public String getId() {
    return buildId(getProjectId(), getDueDate(), getLabelId());
  }

  public Long getProjectId() {
    return Long.valueOf(get("projectId").toString());
  }

  public ViewState setProjectId(Long projectId) {
    put("projectId", projectId);
    return this;
  }

  public String getDueDate() {
    return get("filter").toString();
  }

  public ViewState setDueDate(String dueDate) {
    if (dueDate != null && !dueDate.isEmpty()) {
      put("filter", dueDate);
    } else {
      remove("filter");
    }
    return this;
  }

  public Long getLabelId() {
    return Long.valueOf(get("labelId").toString());
  }

  public ViewState setLabelId(Long labelId) {
    if (labelId != null && labelId != -1L) {
      put("labelId", labelId);
    } else {
      remove("labelId");
    }
    return this;
  }

  public ViewType getViewType() {
    return null;
  }

  public void setViewType(ViewType viewType) {
    put("viewType", viewType.name());
  }

  public Filter getFilter() {
    return new Filter((JSONObject)get("filter"));
  }

  public void setFilter(Filter filter) {
    put("filter", filter);
  }

  public static String buildId(Long projectId, String filter, Long labelId) {
    StringBuilder sBuilder = new StringBuilder("p#").append(projectId);

    if (filter != null) {
      sBuilder.append(".f#").append(filter);
    }

    if (labelId != null) {
      sBuilder.append(".l#").append(labelId);
    }
    return sBuilder.toString();
  }

  public static ViewState createDefaultViewState() {
    return new ViewState();
  }

  public static class Filter extends JSONObject {
    public Filter() {}

    public Filter(JSONObject json) {
      super(json);
    }

    public boolean isEnabled() {
      return Boolean.valueOf(get("enabled").toString());
    }

    public void setEnabled(boolean enabled) {
      put("enabled", enabled);
    }

    public String getKeyword() {
      return get("keyword").toString();
    }

    public void setKeyword(String keyword) {
      put("keyword", keyword);
    }

    public List<Long> getLabel() {
      return (JSONArray) get("labels");
    }

    public void setLabel(List<Long> labels) {
      put("labels", labels);
    }

    public Long getStatus() {
      return Long.valueOf(get("status").toString());
    }

    public void setStatus(Long statusId) {
      if (statusId == null) {
        remove("status");
      } else {
        put("status", statusId);
      }
    }

    public List<String> getAssignee() {
      return (JSONArray) get("assignees");
    }

    public void setAssignee(List<String> assignees) {
      put("assignees", assignees);
    }

    public TaskUtil.DUE getDue() {
      return (TaskUtil.DUE) get(TaskFilterData.FILTER_NAME.DUE);
    }

    public void setDue(TaskUtil.DUE due) {
      if (due == null) {
        remove("due");
      } else {
        put("due", due);
      }
    }

    public Priority getPriority() {
      return Priority.valueOf(get("priority").toString());
    }

    public void setPriority(Priority priority) {
      if (priority == null) {
        remove("priority");
      } else {
        put("priority", priority);
      }
    }

    public boolean isShowCompleted() {
      return Boolean.valueOf(get("showCompleted").toString());
    }

    public void setShowCompleted(boolean bln) {
      put("showCompleted", bln);
    }

    public void updateFilterData(String filterLabelIds,
                                 String statusId,
                                 String dueDate,
                                 String priority,
                                 String assignee,
                                 Boolean showCompleted,
                                 String keyword) {
      if (filterLabelIds != null) {
        List<Long> searchLabelIds = new LinkedList<Long>();
        for (String id : filterLabelIds.split(",")) {
          if (!(id = id.trim()).isEmpty()) {
            try {
              searchLabelIds.add(Long.parseLong(id));
            } catch (Exception ex) {
            }
          }
        }
        this.setLabel(searchLabelIds);
      }

      if (assignee != null) {
        List<String> searchAssignee = new LinkedList<String>();
        for (String u : assignee.split(",")) {
          if (!(u = u.trim()).isEmpty()) {
            searchAssignee.add(u);
          }
        }
        this.setAssignee(searchAssignee);
      }

      if (keyword != null) {
        this.setKeyword(keyword);
      }

      if (statusId != null) {
        if (statusId.isEmpty()) {
          this.setStatus(null);
        } else {
          this.setStatus(Long.parseLong(statusId));
        }
      }

      if (dueDate != null) {
        if (dueDate.isEmpty()) {
          this.setDue(null);
        } else {
          this.setDue(TaskUtil.DUE.valueOf(dueDate.toUpperCase()));
        }
      }
      if (priority != null) {
        if (priority.isEmpty()) {
          this.setPriority(null);
        } else {
          this.setPriority(Priority.valueOf(priority.toUpperCase()));
        }
      }
      if (showCompleted != null) {
        this.setShowCompleted(showCompleted);
      }
    }
  }
}
