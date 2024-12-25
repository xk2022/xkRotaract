package com.xk.ui.service.impl;

import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.po.CmsClub;
import com.xk.ui.model.vo.BubbleInfo;
import com.xk.ui.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the IndexService interface.
 * <p>
 * This class provides the logic to handle index-related operations, including
 * fetching the bubble list for the index page.
 * </p>
 *
 * Key Enhancements:
 * <ul>
 * <li>Improved code readability and maintainability</li>
 * <li>Added support for future local path usage</li>
 * <li>Optimized Base64 encoding handling</li>
 * </ul>
 *
 * @author yuan
 * @version 1.1, Updated on 2024/12/24
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private CmsClubRepository cmsClubRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BubbleInfo> getBubbleList() {
        // Fetch active clubs with logos from the repository
        List<CmsClub> clubs = cmsClubRepository.findActiveClubsWithLogo();

        // Convert CmsClub entities to BubbleInfo objects
        return clubs.stream().map(this::convertToBubbleInfo).collect(Collectors.toList());
    }

    /**
     * Converts a {@link CmsClub} entity to a {@link BubbleInfo} object.
     *
     * @param club the {@link CmsClub} entity
     * @return the corresponding {@link BubbleInfo} object
     */
    private BubbleInfo convertToBubbleInfo(CmsClub club) {
        BubbleInfo bubble = new BubbleInfo();
        bubble.setTitle(club.getName());
        bubble.setLink(String.valueOf(club.getId())); // Set link to the club's detail page

        // Set image URL or Base64 encoded logo
        if (isLocalPathAvailable()) {
//            bubble.setImageUrl(club.getClubLogo()); // Use local path when available
        } else {
            String base64Logo = encodeLogoToBase64(club.getClubLogo());
            bubble.setClubLogoBase64(base64Logo); // Set Base64 logo
        }
        return bubble;
    }

    /**
     * Encodes the club logo as a Base64 string.
     *
     * @param logo the byte array representing the club logo
     * @return the Base64 encoded string, or null if the logo is null
     */
    private String encodeLogoToBase64(byte[] logo) {
        if (logo == null) {
            return null; // Handle null logos gracefully
        }
        return Base64.getEncoder().encodeToString(logo);
    }

    /**
     * Determines if a local path is available for club logos.
     *
     * @return {@code true} if a local path is available; {@code false} otherwise
     */
    private boolean isLocalPathAvailable() {
        // TODO: Replace with actual logic to check for local path availability
        return false;
    }
}
