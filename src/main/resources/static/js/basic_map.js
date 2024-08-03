"use strict";

var customIcon;
var map;
var globalData;
var markers = [];
var infoDiv = document.getElementById('msg');

function initMap() {

    var TaiwanLocation = {
        lat: 23.73602767874198,
        lng: 121.08871396339852
    };


    map = new google.maps.Map(document.getElementById('map'), {
        center: TaiwanLocation,
        zoom: 8
    });

    customIcon = {
        url: 'media/logos/Rotaract_LOGO.png',
        scaledSize: new google.maps.Size(50, 50)
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

    fetch('js/locations.json')
    .then(response => response.json())
    .then(data => {
        globalData = data; // 将数据存储在全局变量中
        // 遍历类型数组
        Object.values(data).forEach(function (locations) {
        // 遍历每个位置对象
            locations.forEach(function (location) {
                const marker = new google.maps.Marker({
                    position: { lat: location.lat, lng: location.lng },
                    map: map,
                    title: location.name,
                    icon: customIcon
                });
                information(location.name, location.description, marker);
                markers.push(marker);
            });
        });
    })
    .catch(error => console.error('Error fetching locations:', error));

}

// show字卡的資料
function information(a, b, marker) {

    marker.addListener('click', function () {
        infoDiv.style.left = '0';
        infoDiv.innerHTML = '<h1>' + a + '</h1><h2>' + b + '</h2>';
    });

    marker.addListener('mouseout', function () {
//        infoDiv.style.left = '-100%';
    })
}

// 當按下checkbox的按鈕
//document.getElementById('btn1').onclick = function () {
//  // 清除地图上的所有标记
//  markers.forEach(marker => {
//    marker.setMap(null);
//  });
//  markers = []; // 清空标记数组
//
//
//  for (let i = 1; i <= 4; i++) {
//    const checkbox = document.getElementById('checkbox' + i);
//    if (checkbox && checkbox.checked) { // 添加对 checkbox 是否存在的检查
//      // 如果复选框被选中，则将其值存到typelocation中
//      var typelocation = checkbox.value;
//      console.log('typelocation:', typelocation);
//      const locations = globalData['type' + i];
//      console.log('位址', locations);
//      locations.forEach(function (location) {
//        // 创建地图标记
//        const marker = new google.maps.Marker({
//          position: { lat: location.lat, lng: location.lng },
//          map: map, // 地图对象
//          title: location.name, // 地点名称
//          icon: customIcon // 自定义图标（可选）
//        });
//
//        // 将标记添加到地图上
//        marker.setMap(map);
//        markers.push(marker);
//
//        information(location.name, location.description, marker);
//      });
//    }
//  }
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