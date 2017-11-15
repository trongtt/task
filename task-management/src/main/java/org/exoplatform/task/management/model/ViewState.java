package org.exoplatform.task.management.model;

import org.exoplatform.task.domain.Priority;
import org.exoplatform.task.util.TaskUtil;

import java.util.*;

public class ViewState {

  private String id;

  public ViewState(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  public ViewType getViewType() {
//    Object viewType = get("viewType");
//    if (viewType != null) {
//      return ViewType.getViewType(viewType.toString());
//    }
    return ViewType.LIST;
  }

  public void setViewType(ViewType viewType) {
//    put("viewType", viewType.name());
  }

  public static String buildId(Long projectId, Long labelId, String filter) {
    StringBuilder sBuilder = new StringBuilder("list@").append(projectId);

    sBuilder.append("@").append(labelId);

    if (filter != null && !filter.isEmpty()) {
      sBuilder.append("@").append(filter);
    }

    return sBuilder.toString();
  }

  public static ViewState createDefaultViewState(String id) {
    return new ViewState(id);
  }

  public String getOrderBy() {
//    Object orderBy = get("orderBy");
//    if (orderBy != null) {
//      return orderBy.toString();
//    } else {
//      return null;
//    }
    return null;
  }

  public void setOrderBy(String orderBy) {
//    put("orderBy", orderBy);
  }

  public String getGroupBy() {
//    Object orderBy = get("groupBy");
//    if (orderBy != null) {
//      return orderBy.toString();
//    } else {
//      return null;
//    }
    return null;
  }

  public void setGroupBy(String groupBy) {
//    put("groupBy", groupBy);
  }

  @Override
  public String toString() {
//    put("filter", filter);
    return super.toString();
  }

  public static class Filter {
    private final String id;
    private boolean enabled;
    private String keyword = "";
    private Long status;
    private boolean completed;
    private List<Long> labels = Collections.emptyList();
    private List<String> assignees = Collections.emptyList();
    private TaskUtil.DUE due;
    private Priority priority;

    public Filter(String id) {
      this.id = id;
    }

    public String getId() {
      return this.id;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getKeyword() {
      return keyword;
    }

    public void setKeyword(String keyword) {
      this.keyword = keyword;
    }

    public List<Long> getLabel() {
      return labels;
    }

    public void setLabel(List<Long> labels) {
      if (labels != null) {
        this.labels = labels;
      }
    }

    public Long getStatus() {
      return status;
    }

    public void setStatus(Long statusId) {
      this.status = statusId;
    }

    public List<String> getAssignees() {
      return assignees;
    }

    public void setAssignee(List<String> assignees) {
      if (assignees != null) {
        this.assignees = assignees;
      }
    }

    public TaskUtil.DUE getDue() {
      return due;
    }

    public void setDue(TaskUtil.DUE due) {
      this.due = due;
    }

    public Priority getPriority() {
      return priority;
    }

    public void setPriority(Priority priority) {
      this.priority = priority;
    }

    public boolean isShowCompleted() {
      return completed;
    }

    public void setShowCompleted(boolean bln) {
    }

    public void updateFilterData(String filterLabelIds,
                                 String statusId,
                                 String dueDate,
                                 String priority,
                                 String assignee,
                                 Boolean showCompleted,
                                 String keyword) {
      if (filterLabelIds != null) {
        List<Long> searchLabelIds = new ArrayList<>();
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
        List<String> searchAssignee = new ArrayList<>();
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
