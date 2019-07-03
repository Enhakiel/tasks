package org.tasks.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.todoroo.astrid.data.Task;
import com.todoroo.astrid.helper.UUIDHelper;
import java.util.List;
import org.tasks.filters.TagFilters;

@Dao
public abstract class TagDataDao {

  @Query("SELECT * FROM tagdata")
  public abstract LiveData<List<TagData>> subscribeToTags();

  @Query("SELECT * FROM tagdata WHERE name = :name COLLATE NOCASE LIMIT 1")
  public abstract TagData getTagByName(String name);

  @Query("SELECT * FROM tagdata")
  public abstract List<TagData> getAll();

  @Query("SELECT * FROM tagdata WHERE remoteId = :uuid LIMIT 1")
  public abstract TagData getByUuid(String uuid);

  @Query("SELECT * FROM tagdata WHERE name IS NOT NULL ORDER BY UPPER(name) ASC")
  public abstract List<TagData> tagDataOrderedByName();

  @Query("UPDATE tagdata SET name = :name WHERE remoteId = :remoteId")
  public abstract void rename(String remoteId, String name);

  @Query("DELETE FROM tagdata WHERE _id = :id")
  public abstract void delete(Long id);

  @Update
  public abstract void update(TagData tagData);

  @Insert
  abstract long insert(TagData tag);

  public void createNew(TagData tag) {
    if (Task.isUuidEmpty(tag.getRemoteId())) {
      tag.setRemoteId(UUIDHelper.newUUID());
    }
    tag.setId(insert(tag));
  }

  @Query(
      "SELECT tagdata.*, COUNT(tasks._id) AS count"
          + " FROM tagdata"
          + " LEFT JOIN tags ON tags.tag_uid = tagdata.remoteId"
          + " LEFT JOIN tasks ON tags.task = tasks._id AND tasks.deleted = 0 AND tasks.completed = 0 AND tasks.hideUntil = 0"
          + " WHERE tagdata.name IS NOT NULL AND tagdata.name != ''"
          + " GROUP BY tagdata.remoteId"
          + " ORDER BY tagdata.name COLLATE NOCASE")
  public abstract List<TagFilters> getTagFilters();
}
