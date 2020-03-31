/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxlabaza.test.repeated.list.items;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import lombok.NonNull;

interface DuplicatesFinder {

  DuplicatesFinder IN_ROW = new FinderInRow();

  DuplicatesFinder NOT_IN_ROW = new FinderNotInRow();

  List<Integer> find (List<Integer> list);

  class FinderInRow implements DuplicatesFinder {

    @Override
    public List<Integer> find (@NonNull List<Integer> list) {
      if (list.isEmpty() || list.size() < 3) {
        return emptyList();
      }
      return list.stream()
          .collect(groupingBy(it -> it, counting()))
          .entrySet()
          .stream()
          .filter(it -> it.getValue() > 2)
          .map(Entry::getKey)
          .collect(toList());
    }
  }

  class FinderNotInRow implements DuplicatesFinder {

    DuplicatesFinder delegate = new FinderInRow();

    @Override
    public List<Integer> find (@NonNull List<Integer> list) {
      Collections.sort(list);
      return delegate.find(list);
    }
  }
}
