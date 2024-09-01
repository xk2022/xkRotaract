"use strict";

var customIcon;
var map;
var globalData;
var markers = [];
var infoDiv = document.getElementById('msg');
var drawerOpen = false;

function initMap() {

    // 定義中心位置
    var TaiwanLocation = {
        lat: 23.73602767874198,
        lng: 121.08871396339852
    };

    // 創建地圖
    map = new google.maps.Map(document.getElementById('map'), {
        center: TaiwanLocation,
        zoom: 8 // 設置縮放級別
    });

    function calculateIconSize() {
        // 根据容器或窗口的实际尺寸计算图标的大小
    }
    // 计算图标大小并向上取整到最接近的十位数
    function calculateIconSize() {
//        return new google.maps.Size(window.innerWidth / 20, window.innerWidth / 20); // 例子：图标大小为窗口宽度的 1/20
        const width = Math.ceil(window.innerWidth / 25);
        let roundedSize = Math.round(width / 10) * 10; // 向十位数进位
        if (width < 40) {
            roundedSize = 40;
        } else {
            roundedSize = 50;
        }
        return new google.maps.Size(roundedSize, roundedSize); // 高度与宽度相同
    }
    // 自定義圖標
    customIcon = {
        url: 'media/logos/Rotaract_LOGO.png',   // 自定義圖標 URL
        // scaledSize: new google.maps.Size(30, 30)    // 圖標大小
        scaledSize: calculateIconSize()    // 动态计算图标大小
    };

    var customMapType = new google.maps.StyledMapType(
        [
          // 地图要素类型：道路
          {
            featureType: 'road',
            elementType: 'geometry',
            stylers: [
              { color: '#f7f7f7' }, // 道路颜色：浅灰色
              { visibility: 'simplified' } // 简化道路元素
            ]
          },
          // 地图要素类型：水域
          {
            featureType: 'water',
            elementType: 'geometry',
            stylers: [
              { color: '#a2daf2' } // 水域颜色：淡蓝色
            ]
          },
          // 地图要素类型：景观
          {
            featureType: 'landscape',
            elementType: 'geometry',
            stylers: [
              { color: '#ebebeb' } // 景观颜色：浅灰色
            ]
          },
          // 关闭兴趣点标签显示
          {
            featureType: 'poi',
            elementType: 'labels',
            stylers: [
              { visibility: 'off' }
            ]
          },
          // 关闭地图标签显示
          {
            featureType: 'administrative',
            elementType: 'labels',
            stylers: [
              { visibility: 'off' }
            ]
          }
        ],
        { name: '简化舒适地图样式' }
      );
      // 将自定义地图类型添加到地图中
      map.mapTypes.set('custom_style', customMapType);

      // 设置地图显示的类型为自定义地图类型
      map.setMapTypeId('custom_style');

//    fetch('js/locations.json')
//    .then(response => response.json())
//    .then(data => {
//        globalData = data; // 将数据存储在全局变量中
//        // 遍历类型数组
//        Object.values(data).forEach(function (locations) {
//        // 遍历每个位置对象
//            locations.forEach(function (location) {
//                const marker = new google.maps.Marker({
//                    position: { lat: location.lat, lng: location.lng },
//                    map: map,
//                    title: location.name,
//                    icon: customIcon
//                });
//                information(location.name, location.description, marker);
//                markers.push(marker);
//            });
//        });
//    })
//    .catch(error => console.error('Error fetching locations:', error));

//    /api/manage/index/locationCompany

//        .catch(error => console.error('Error fetching locations:', error));

    $.ajax({
        url: '/xkRotaract/api/manage/index/locationCompany',
        method: 'GET',
        data: '', // 修改为适合你的代码
        processData: false,
        contentType: 'application/json',
        success: function(response) {
            console.log('AJAX 请求成功：', response);

            var data = response.locations;
            globalData = data; // 将数据存储在全局变量中
            // 遍历类型数组
            Object.values(data).forEach(function (locations) {
            // 遍历每个位置对象
                locations.forEach(function (location) {
                    // 創建標記
                    const marker = new google.maps.Marker({
                        position: { lat: Number(location.lat), lng: Number(location.lng) },
                        map: map,
                        title: location.locId,
                        icon: customIcon
                    });
                    information(location.name, location.description, marker);
                    markers.push(marker);
                });
            });// 获取下拉选单元素
        },
        error: function(xhr, status, error) {
            console.error('AJAX 请求失败：', error);
        }
    });

}

// show字卡的資料
function information(a, b, marker) {

    // 添加點擊事件監聽器
    marker.addListener('click', function() {

        // Populate the page loading element dynamically.
        // Optionally you can skipt this part and place the HTML
        // code in the body element by refer to the above HTML code tab.
        const loadingEl = document.createElement("div");
        document.body.prepend(loadingEl);
        loadingEl.classList.add("page-loader");
        loadingEl.classList.add("flex-column");
        loadingEl.classList.add("bg-dark");
        loadingEl.classList.add("bg-opacity-25");
        loadingEl.innerHTML = `
            <span class="spinner-border text-primary" role="status"></span>
            <span class="text-gray-800 fs-6 fw-semibold mt-5">Loading...</span>
        `;


        modal.hide(); // Hide modal
        form.reset(); // Reset form
        const companyId = this.title; // 从标记的 title 属性获取 companyId
        fetchInformation(companyId).then(data => {
            updateDrawerContent(data);
            toggleVisibility(false);
            modal.show(); // Hide modal
//            toggleDrawer();
        });
    });

//    marker.addListener('click', function () {
//        infoDiv.style.left = '0';
//        infoDiv.innerHTML = '<h1>' + a + '</h1><h2>' + b + '</h2>';
//    });

    marker.addListener('mouseout', function () {
//        infoDiv.style.left = '-100%';
    })
}

function fetchInformation(companyId) {
    return $.ajax({
        url: '/xkRotaract/api/manage/company/findInfo',
        method: 'POST',
        data: JSON.stringify({ 'id': companyId }), // 修改为适合你的代码
        processData: false,
        contentType: 'application/json',
        success: function(response) {
            console.log('AJAX 请求成功：', response);
            return response; // 返回从服务器获取的数据
        },
        error: function(xhr, status, error) {
            console.error('AJAX 请求失败：', error);
            return { personal: {}, company: {} }; // 处理错误时的默认值
        }
    });
}

function updateDrawerContent(data) {
//    $('#rotaract_name').text(data.rotaract_name);
//    $('#rname').text(data.rname);
//
//    if (data.locked) {
//        $('#ciSection').hide();
//    } else {
//        $('#ciSection').show();
//
//        $('#name').text(data.name);
//        $('#phone').text(data.phone);
//        $('#address').text(data.address);
//        // 更新 #url 的文本和 href 属性
//        if (data.url) {
//            $('#url').text(data.url);
//            $('#url').parent('a').attr('href', data.url); // 更新父 <a> 标签的 href 属性
//        } else {
//            $('#url').text('No URL available');
//            $('#url').parent('a').removeAttr('href'); // 移除 href 属性，防止点击
//        }
//    }
        $('#rotaract_name').val(data.rotaract_name);
        $('#rname').val(data.rname);

        if (data.locked) {
            $('#ciSection').hide();
        } else {
            $('#ciSection').show();

            $('#name').val(data.name);
            $('#phone').val(data.phone);
            $('#address').val(data.address);
            // 更新 #url 的文本和 href 属性
            if (data.url) {
                $('#url').text(data.url);
                $('#url').parent('a').attr('href', data.url); // 更新父 <a> 标签的 href 属性
            } else {
                $('#url').text('No URL available');
                $('#url').parent('a').removeAttr('href'); // 移除 href 属性，防止点击
            }
        }
}

function toggleDrawer() {
    const drawer = document.getElementById('drawer');
    drawerOpen = !drawerOpen;
    drawer.classList.toggle('open', drawerOpen);
}

//function closeDrawer() {
//    drawerOpen = false;
//    document.getElementById('drawer').classList.remove('open');
//}

// Add event listener to button
//document.getElementById('drawerToggleButton').addEventListener('click', toggleDrawer);


function handleButtonClick() {
    // JavaScript function code here
//    alert('按鈕被點擊了！');

    // 其他代碼...
    // 清除地图上的所有标记
    markers.forEach(marker => {
        marker.setMap(null);
    });
    markers = []; // 清空标记数组

    // 獲取 globalData 的鍵數量
    const size = Object.keys(globalData).length;

    console.log(size); // 19
    for (let i = 1; i <= size; i++) {
        const checkbox = document.getElementById('flexCheckDefault_' + i);
        if (checkbox && checkbox.checked) { // 添加对 checkbox 是否存在的检查
            // 如果复选框被选中，则将其值存到typelocation中
            var typelocation = checkbox.value;
            console.log('typelocation:', typelocation);
            const locations = globalData[i];
            console.log('位址', locations);

            locations.forEach(function (location) {
                // 创建地图标记
                const marker = new google.maps.Marker({
                    position: { lat: Number(location.lat), lng: Number(location.lng) },
                    map: map, // 地图对象
                    title: location.locId,
                    icon: customIcon // 自定义图标（可选）
                });
                // 将标记添加到地图上
                information(location.name, location.description, marker);
                markers.push(marker);
                marker.setMap(map);
            });
        }
    }

    modal.hide();
}
// 當按下checkbox的按鈕
//document.getElementById('btn1').onclick = function () {

//
//
//

//}
//    // 嘗試獲取使用者的位置
//    if (navigator.geolocation) {
//        navigator.geolocation.getCurrentPosition(function(position) {
//            // 獲取使用者的經緯度
//            var userLocation = {
//                lat: position.coords.latitude,
//                lng: position.coords.longitude
//            };
//
//            var infoDiv = document.getElementById('msg'); // 取得要顯示的 div 元素
//            // 初始化地圖，以使用者的位置為中心
//            var map = new google.maps.Map(document.getElementById('map'), {
//                center: userLocation,
//                zoom: 12
//            });
//
//        var customIcon = {
//            url: 'Rotaract_LOGO.png',  // 換成你的圖標檔案路徑
//            scaledSize: new google.maps.Size(100, 100)  // 設定圖標大小
//
//        };
//
//        fetch('locations.json')
//        .then(response => response.json())
//        .then(locations => {
//            // 在地圖上標記每個位置
//            locations.forEach(function(location) {
//                var marker = new google.maps.Marker({
//                    position: {lat: location.lat, lng: location.lng},
//                    map: map,
//                    title: location.name,
//                    icon: customIcon
//                });
//
//                // 在標記上添加信息窗口
//                // var infowindow = new google.maps.InfoWindow({
//                //     content: '<h3>' + location.name + '</h3><p>' + location.description + '</p>',
//                //     maxWidth: 500  // 設定信息窗口的最大寬度
//                // });
//
//                // 為標記添加點擊事件，顯示信息窗口
//                marker.addListener('click', function() {
//                    // infowindow.open(map, marker);
//                    infoDiv.style.left = '0';
//                    //把infoDiv顯示
//                    // infoDiv.style.display = 'block';
//
//                    // 更新 infoDiv 的內容
//                    infoDiv.innerHTML = '<h1>' + location.name + '</h1><h2>' + location.description + '</h2>';
//                });
//
//                marker.addListener('mouseout', function() {
//                    // infowindow.close();
//                    // infoDiv.style.display = 'none';
//                    infoDiv.style.left = '-100%';
//
//                });
//            });
//        })
//        .catch(error => console.error('Error fetching locations:', error));
//    }, function() {
//    // 無法獲取使用者位置時的處理
//    handleLocationError(true);
//    });
//    } else {
//    // 瀏覽器不支援地理定位時的處理
//    handleLocationError(false);
//    }
//    // // 點擊地圖其他區域時隱藏 infoDiv
//    // map.addListener('click', function() {
//    //     infoDiv.style.display = 'none';
//    // });
//    }
//
//    function handleLocationError(browserHasGeolocation) {
//    var errorMsg = browserHasGeolocation ? '定位失敗。' : '您的瀏覽器不支援地理定位。';
//    alert(errorMsg);
//    }