package org.exoplatform.task.management.model;

import org.exoplatform.task.domain.Priority;
import org.exoplatform.task.util.TaskUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class ViewState extends JSONObject {

  private String id;
  private Filter filter;
  private String groupBy;

  public ViewState(String id) {
    this.id = id;
    filter = new Filter();
  }

  public ViewState(String id, JSONObject json) {
    super(json);
    this.id = id;

    filter = new Filter();
  }

  public String getId() {
    if (id == null) {
      id = buildId(getProjectId(), getDueDate(), getLabelId());
    }
    return id;
  }

  public Long getProjectId() {
    Object projectId = get("projectId");
    if (projectId != null) {
      return Long.valueOf(projectId.toString());
    } else {
      return null;
    }
  }

  public ViewState setProjectId(Long projectId) {
    put("projectId", projectId);
    return this;
  }

  public String getDueDate() {
    Object filter = get("filter");
    if (filter != null) {
      return filter.toString();
    } else {
      return null;
    }
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
    Object labelId = get("labelId");
    if (labelId != null) {
      return Long.valueOf(labelId.toString());
    } else {
      return null;
    }
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
    Object viewType = get("viewType");
    if (viewType != null) {
      return ViewType.getViewType(viewType.toString());
    }
    return ViewType.LIST;
  }

  public void setViewType(ViewType viewType) {
    put("viewType", viewType.name());
  }

  public Filter getFilter() {
    return filter;
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

  public static ViewState createDefaultViewState(String id) {
    return new ViewState(id);
  }

  public String getOrderBy() {
    Object orderBy = get("orderBy");
    if (orderBy != null) {
      return orderBy.toString();
    } else {
      return null;
    }
  }

  public void setOrderBy(String orderBy) {
    put("orderBy", orderBy);
  }

  public String getGroupBy() {
    Object orderBy = get("groupBy");
    if (orderBy != null) {
      return orderBy.toString();
    } else {
      return null;
    }
  }

  public void setGroupBy(String groupBy) {
    put("groupBy", groupBy);
  }

  public static class Filter extends JSONObject {
    public Filter() {}

    public Filter(JSONObject json) {
      super(json);
    }

    public boolean isEnabled() {
      Object enabled = get("enabled");
      if (enabled != null) {
        return Boolean.valueOf(enabled.toString());
      } else {
        return false;
      }
    }

    public void setEnabled(boolean enabled) {
      put("enabled", enabled);
    }

    public String getKeyword() {
      Object keyword = get("keyword");
      if (keyword != null) {
        return keyword.toString();
      } else {
        return "";
      }
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
      Object status = get("status");
      if (status != null) {
        return Long.valueOf(get("status").toString());
      } else {
        return null;
      }
    }

    public void setStatus(Long statusId) {
      if (statusId == null) {
        remove("status");
      } else {
        put("status", statusId);
      }
    }

    public List<String> getAssignee() {
      return null;
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
      Object priority = get("priority");
      if (priority != null) {
        return Priority.valueOf(priority.toString());
      } else {
        return null;
      }
    }

    public void setPriority(Priority priority) {
      if (priority == null) {
        remove("priority");
      } else {
        put("priority", priority);
      }
    }

    public boolean isShowCompleted() {
      Object showCompleted = get("showCompleted");
      if (showCompleted != null) {
        return Boolean.valueOf(showCompleted.toString());
      } else {
        return false;
      }
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
