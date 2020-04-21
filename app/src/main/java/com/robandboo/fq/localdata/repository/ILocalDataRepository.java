package com.robandboo.fq.localdata.repository;

import com.robandboo.fq.localdata.entity.ILocalData;

public interface ILocalDataRepository <T extends ILocalData> {
    void save(T data);
    void deleteByLocalId(int local_id);
    void deleteByServerId(int server_id);
    int loadLocalIdByServerId(int serverId);
    int loadServerIdByLocalId(int localId);
}
