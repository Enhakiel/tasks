/*
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */

package com.todoroo.astrid.tags;

import static com.google.common.collect.Lists.transform;

import com.google.common.base.Strings;
import com.todoroo.astrid.api.Filter;
import com.todoroo.astrid.api.TagFilter;
import java.util.List;
import javax.inject.Inject;
import org.tasks.data.TagData;
import org.tasks.data.TagDataDao;
import org.tasks.filters.TagFilters;

/**
 * Exposes filters based on tags
 *
 * @author Tim Su <tim@todoroo.com>
 */
public class TagFilterExposer {

  private final TagService tagService;
  private final TagDataDao tagDataDao;

  @Inject
  public TagFilterExposer(TagDataDao tagDataDao, TagService tagService) {
    this.tagDataDao = tagDataDao;
    this.tagService = tagService;
  }

  /** Create filter from new tag object */
  private static TagFilter filterFromTag(TagData tag) {
    if (tag == null || Strings.isNullOrEmpty(tag.getName())) {
      return null;
    }
    return new TagFilter(tag);
  }

  public List<Filter> getFilters() {
    return transform(tagDataDao.getTagFilters(), TagFilters::toTagFilter);
  }

  public Filter getFilterByUuid(String uuid) {
    return filterFromTag(tagService.tagFromUUID(uuid));
  }
}
