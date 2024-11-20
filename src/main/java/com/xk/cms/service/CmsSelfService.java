package com.xk.cms.service;

import com.xk.common.json.Industry;
import java.util.List;

/**
 * CmsSelf Service interface
 * <p>
 * 該接口提供了個人管理模塊中與行業信息相關的服務方法。
 * 通過此接口，可以查詢所有行業信息，以及將行業信息分組的功能。
 * </p>
 *
 * <h2>設計背景:</h2>
 * <p>
 * 主要為個人管理模塊提供行業數據的支撐，使得用戶能夠查詢到完整的行業列表，
 * 並可以以分組方式展示行業，方便前端進行多列佈局等操作。
 * </p>
 *
 * @author yuan Created on 2024/05/02.
 * @author yuan Updated on 2024/11/01.
 *
 * @param <Industry> 行業類型，用於表示單個行業的數據
 */
public interface CmsSelfService {

    /**
     * 查詢所有行業信息
     * 返回所有行業的數據，便於用戶檢索和展示。該方法的具體實現應該從數據庫或其他數據源獲取行業信息。
     *
     * @return 包含所有行業的列表
     */
    List<Industry> getAllIndustries();

    /**
     * 將行業信息進行分組，用於多列佈局顯示
     * 該方法將所有行業信息進行分組，以便在前端以多列顯示。返回值是一個嵌套列表，每個子列表包含一部分行業數據。
     * 分組邏輯可以根據具體需求來實現，例如按固定大小分組。
     *
     * @return 分組後的行業列表，每個子列表包含一部分行業數據
     */
    List<List<Industry>> getChunkedIndustries();

}
