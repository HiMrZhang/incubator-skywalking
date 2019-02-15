/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.collector.storage.dao.ui;

import org.apache.skywalking.apm.collector.storage.base.dao.DAO;
import org.elasticsearch.action.index.IndexRequestBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Interface to be implemented for execute database query operation
 * from {@link org.apache.skywalking.apm.collector.storage.table.alarm.ApplicationAlarmTable#TABLE}.
 *
 * @author peng-yongsheng
 * @see org.apache.skywalking.apm.collector.storage.StorageModule
 */
public interface IRApplicationAlarmContactUIDAO extends DAO {


    Long deleteByApplicationId(Integer applicationId);

    List<RApplicationAlarmContact> getByApplicationId(Integer applicationId);

    IndexRequestBuilder prepareBatchInsert(IRApplicationAlarmContactUIDAO.RApplicationAlarmContact rApplicationAlarmContact) throws IOException;

    Long deleteByAlarmContactId(Integer alarmContactId);

    void batchPersistence(List<IndexRequestBuilder> collection);

    class RApplicationAlarmContact {
        private String id;
        private int status;
        private Long createTime;
        private Long updateTime;
        private Integer alarmContactId;
        private Integer applicationId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public Integer getAlarmContactId() {
            return alarmContactId;
        }

        public void setAlarmContactId(Integer alarmContactId) {
            this.alarmContactId = alarmContactId;
        }

        public Integer getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(Integer applicationId) {
            this.applicationId = applicationId;
        }
    }
}
