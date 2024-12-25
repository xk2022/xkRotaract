package com.xk.ui.service;

import com.xk.ui.model.vo.BubbleInfo;

import java.util.List;

/**
 * Service interface for managing the index-related operations.
 * <p>
 * This interface provides methods to handle operations like retrieving bubble
 * information and other content displayed on the index page.
 * </p>
 *
 * <p>
 * Key Features:
 * <ul>
 * <li>Encapsulation of business logic for index content management</li>
 * <li>Support for retrieving and displaying bubble information</li>
 * </ul>
 * </p>
 *
 * @author yuan
 * @version 1.2, Updated on 2024/12/24
 */
public interface IndexService {

    /**
     * Retrieves a list of bubble information to be displayed on the index page.
     *
     * @return a list of {@link BubbleInfo} representing bubble items.
     */
    List<BubbleInfo> getBubbleList();
}
