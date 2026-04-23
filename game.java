let scene, camera, renderer, controls;
let objects = [];
let raycaster;

init();
animate();

function init() {
  scene = new THREE.Scene();
  scene.background = new THREE.Color(0x202020);

  camera = new THREE.PerspectiveCamera(75, window.innerWidth/window.innerHeight, 1, 1000);

  renderer = new THREE.WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);

  controls = new THREE.PointerLockControls(camera, document.body);

  document.addEventListener('click', () => {
    controls.lock();
  });

  scene.add(controls.getObject());

  // Floor
  const floor = new THREE.Mesh(
    new THREE.PlaneGeometry(200, 200),
    new THREE.MeshBasicMaterial({ color: 0x555555 })
  );
  floor.rotation.x = -Math.PI / 2;
  scene.add(floor);

  // Targets (enemies)
  const geometry = new THREE.BoxGeometry(2, 2, 2);
  const material = new THREE.MeshBasicMaterial({ color: 0xff0000 });

  for (let i = 0; i < 20; i++) {
    const cube = new THREE.Mesh(geometry, material);
    cube.position.set(
      Math.random() * 100 - 50,
      1,
      Math.random() * 100 - 50
    );
    scene.add(cube);
    objects.push(cube);
  }

  raycaster = new THREE.Raycaster();

  document.addEventListener('mousedown', shoot);
}

function shoot() {
  raycaster.setFromCamera(new THREE.Vector2(0, 0), camera);
  const intersects = raycaster.intersectObjects(objects);

  if (intersects.length > 0) {
    const hit = intersects[0].object;
    scene.remove(hit);
    objects.splice(objects.indexOf(hit), 1);
  }
}

let moveForward = false;
let moveBackward = false;
let moveLeft = false;
let moveRight = false;

document.addEventListener('keydown', (e) => {
  switch (e.code) {
    case 'KeyW': moveForward = true; break;
    case 'KeyS': moveBackward = true; break;
    case 'KeyA': moveLeft = true; break;
    case 'KeyD': moveRight = true; break;
  }
});

document.addEventListener('keyup', (e) => {
  switch (e.code) {
    case 'KeyW': moveForward = false; break;
    case 'KeyS': moveBackward = false; break;
    case 'KeyA': moveLeft = false; break;
    case 'KeyD': moveRight = false; break;
  }
});

function animate() {
  requestAnimationFrame(animate);

  const speed = 0.1;

  if (moveForward) controls.moveForward(speed);
  if (moveBackward) controls.moveForward(-speed);
  if (moveLeft) controls.moveRight(-speed);
  if (moveRight) controls.moveRight(speed);

  renderer.render(scene, camera);
}
