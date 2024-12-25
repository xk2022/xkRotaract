const container = document.querySelector('.bubble-container');

// 動態生成氣泡
function createBubble(bubbleInfo) {
    const bubble = document.createElement('div');
    bubble.className = 'bubble';

    const link = document.createElement('a');
    link.href = bubbleInfo.link; // 使用 API 返回的外部連結
    link.target = '_blank';

    const logo = document.createElement('img');

    // 檢查是否有 Base64 編碼的 LOGO 或圖片地址
    if (bubbleInfo.clubLogoBase64) {
        logo.src = `data:image/png;base64,${bubbleInfo.clubLogoBase64}`; // 使用 Base64
    } else if (bubbleInfo.imageUrl) {
        logo.src = bubbleInfo.imageUrl; // 使用圖片地址
    } else {
        console.error('缺少圖片數據');
        return; // 跳過無效數據
    }

    logo.alt = bubbleInfo.title; // 使用 API 返回的標題作為替代文字

    link.appendChild(logo);
    bubble.appendChild(link);

    // 隨機設定位置和動畫時間
    bubble.style.left = `${Math.random() * 100}%`;
    bubble.style.animationDuration = `${8 + Math.random() * 4}s`;

    container.appendChild(bubble);

    // 一段時間後移除氣泡以節省內存
    setTimeout(() => bubble.remove(), 12000);
}

// 通過 AJAX 獲取泡泡球數據
function fetchBubbleList() {
    fetch('/xkRotaract/api/manage/index/bubbles') // 替換為你的 API 路徑
        .then(response => response.json())
        .then(data => {
            if (Array.isArray(data)) {
                // 每隔一段時間生成一個氣泡
                let index = 0;
                setInterval(() => {
                    if (index < data.length) {
                        createBubble(data[index]);
                        index++;
                    } else {
                        index = 0; // 循環顯示氣泡
                    }
                }, 3000);
            } else {
                console.error('返回數據格式不正確');
            }
        })
        .catch(error => console.error('獲取泡泡球列表失敗:', error));
}

// 初始化氣泡功能
fetchBubbleList();


/*根据你的需求，LOGO 分为两种类别：社团 LOGO 和公司 LOGO，并且目前数量相同（各 10 个），最大值也已确定。以下是关于轮播泡泡的时间和方式的建议：

  ---

  ### **轮播时间建议**

  1. **轮播频率**：
     - 假设每次显示一个 LOGO 的泡泡，建议生成间隔为 `2-3 秒`，可以通过 `setInterval` 实现。
     - 根据总数量动态调整，确保在合理时间内循环展示一遍：
       - 社团 LOGO：`10 * 3 = 30 秒` 完成一轮。
       - 公司 LOGO：`10 * 3 = 30 秒` 完成一轮。

  2. **LOGO 类型切换**：
     - 交替显示社团 LOGO 和公司 LOGO。
     - 比如，前 15 秒为社团 LOGO，后 15 秒为公司 LOGO。

  3. **可调节时间**：
     - 随着 LOGO 数量增加，保持总轮播时间相对稳定，调整每个 LOGO 的显示时间。
     - 例如：
       - 社团 LOGO 达到 180 时，每个 LOGO 显示时间可以缩短至 `1 秒`，完成一轮需要 `180 秒`。
       - 公司 LOGO 达到 500 时，每个 LOGO 显示时间可以调整至 `0.5 秒`，完成一轮需要 `250 秒`。

  ---

  ### **轮播方式建议**

  1. **交替显示模式**：
     - 按类别分批显示：先显示社团 LOGO 的泡泡，再显示公司 LOGO 的泡泡。
     - 使用队列管理 LOGO 列表：
       - 第一轮：从社团 LOGO 队列依次取出。
       - 第二轮：从公司 LOGO 队列依次取出。

  2. **随机混合模式**：
     - 将社团 LOGO 和公司 LOGO 混合随机排序后显示。
     - 随机选择 LOGO，但仍保持两种 LOGO 的均衡显示。

  3. **优先级模式**：
     - 根据当前页面的受众或活动的优先级，动态调整 LOGO 的显示比例：
       - 如果社团相关活动优先，则社团 LOGO 显示 70%，公司 LOGO 显示 30%。
       - 如果活动偏向商业推广，则反之。

  4. **动态加速模式**：
     - LOGO 列表短时，以较长的时间间隔展示（如 3 秒）。
     - LOGO 列表变长后，缩短间隔（如 1 秒），避免用户等待过久。

  ---

  ### **具体实现方案**

  以下是基于交替显示模式的实现代码示例：

  #### **JavaScript 动态轮播实现**

  ```javascript
  const container = document.querySelector('.bubble-container');

  // LOGO 列表
  const clubLogos = [ /* 社團LOGO數據 *\ ];
  const companyLogos = [ /* 公司LOGO數據 *\ ];

  let currentType = 'club'; // 当前显示类型：'club' 或 'company'
  let clubIndex = 0;        // 社团 LOGO 当前索引
  let companyIndex = 0;     // 公司 LOGO 当前索引

  // 动态生成气泡
  function createBubble(logo) {
      const bubble = document.createElement('div');
      bubble.className = 'bubble';

      const link = document.createElement('a');
      link.href = logo.link || '#'; // LOGO 链接
      link.target = '_blank';

      const img = document.createElement('img');
      img.src = logo.clubLogoBase64
          ? `data:image/png;base64,${logo.clubLogoBase64}`
          : logo.imageUrl; // LOGO 图片地址或 Base64
      img.alt = logo.title;

      link.appendChild(img);
      bubble.appendChild(link);

      // 设置随机位置和动画时间
      bubble.style.left = `${Math.random() * 100}%`;
      bubble.style.animationDuration = `${8 + Math.random() * 4}s`;

      container.appendChild(bubble);

      // 一段时间后移除气泡
      setTimeout(() => bubble.remove(), 12000);
  }

  // 显示 LOGO
  function showLogo() {
      if (currentType === 'club') {
          if (clubIndex < clubLogos.length) {
              createBubble(clubLogos[clubIndex]);
              clubIndex++;
          } else {
              // 切换到公司 LOGO
              currentType = 'company';
              clubIndex = 0;
          }
      } else if (currentType === 'company') {
          if (companyIndex < companyLogos.length) {
              createBubble(companyLogos[companyIndex]);
              companyIndex++;
          } else {
              // 切换到社团 LOGO
              currentType = 'club';
              companyIndex = 0;
          }
      }
  }

  // 开始轮播
  setInterval(showLogo, 3000); // 每隔 3 秒生成一个气泡
  ```

  ---

  ### **优化方式**

  1. **性能优化**：
     - 如果 LOGO 数量很多，可以通过懒加载的方式按需加载 LOGO 数据。

  2. **动态调节**：
     - 根据用户操作或页面流量动态调整轮播间隔和显示比例。

  3. **视觉增强**：
     - 添加 LOGO 动画效果，例如透明度渐变、缩放等。

  ---

  ### **结论**

  - **时间**：建议 `2-3 秒` 一次气泡生成，满足用户关注度的同时不显得过于拥挤。
  - **方式**：推荐交替显示或随机混合，兼顾平衡和灵活性。
  - **实现**：基于队列按需生成，避免 DOM 累积过多，提升性能。

  如需更多具体调整，请告诉我！*/