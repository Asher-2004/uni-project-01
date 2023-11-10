function showImage(state) {
  const imageMap = {
    'New South Wales': 'states16/New South Wales.png',
    'Queensland': 'states16/Queensland.png',
    'South Australia': 'path_to_your_image_3',
    'Tasmania': 'path_to_your_image_4',
    'Victoria': 'path_to_your_image_5',
    'Western Australia': 'path_to_your_image_6',
    'Northern Territory': 'states16/Northern Territory.png',
    'Australian Capital Territory': 'states16/Australian Capital Territory.png'
  };
  document.getElementById('stateImage').src = imageMap[state];
}
