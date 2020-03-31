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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DuplicatesFinderTests {

  @Nested
  class FinderInRowTests {

    DuplicatesFinder finder = DuplicatesFinder.IN_ROW;

    @Test
    void nullCheck () {
      assertThatThrownBy(() -> finder.find(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void noMatches () {
      Stream.of(
          new ArrayList<Integer>(),
          asList(1),
          asList(1, 1),
          asList(1, 1, 2, 2, 3, 3),
          asList(1, 2, 2, 3, 3, 4)
      ).forEach(list -> {
        assertThat(finder.find(list))
            .as("check list %s", list)
            .isEmpty();
      });
    }

    @Test
    void singleMatch () {
      Stream.of(
          asList(1, 1, 1),
          asList(0, 1, 1, 1),
          asList(1, 1, 1, 2),
          asList(0, 1, 1, 1, 2),
          asList(1, 1, 1, 1)
      ).forEach(list -> {
        assertThat(finder.find(list))
            .as("check list %s", list)
            .containsExactlyInAnyOrder(1);
      });
    }

    @Test
    void twoMatches () {
      Stream.of(
          asList(1, 1, 1, 2, 2, 2),
          asList(0, 1, 1, 1, 2, 2, 2),
          asList(1, 1, 1, 2, 2, 2, 3),
          asList(0, 1, 1, 1, 0, 2, 2, 2, 3),
          asList(1, 1, 1, 1, 2, 2, 2, 2)
      ).forEach(list -> {
        assertThat(finder.find(list))
            .as("check list %s", list)
            .containsExactlyInAnyOrder(1, 2);
      });
    }
  }

  @Nested
  class FinderNotInRowTests extends FinderInRowTests {

    {
      finder = DuplicatesFinder.NOT_IN_ROW;
    }

    @Test
    void testUnorderedLists () {
      Stream.of(
          asList(1, 2, 2, 1, 2, 1),
          asList(0, 1, 1, 2, 2, 1, 2),
          asList(2, 1, 2, 1, 1, 2, 3),
          asList(3, 0, 1, 0, 1, 2, 1, 2, 2),
          asList(7, 7, 1, 2, 2, 1, 5, 1, 2, 2, 1)
      ).forEach(list -> {
        assertThat(finder.find(list))
            .as("check list %s", list)
            .containsExactlyInAnyOrder(1, 2);
      });
    }
  }
}
