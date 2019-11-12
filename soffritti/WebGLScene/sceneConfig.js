const config = {
  floor: {
    size: { x: 20, y: 20 }
  },
  player: {
    // position: { x: 0.5, y: 0.5 },	// CENTER
    position: { x: 0.12, y: 0.12 },		// INIT
    // position: { x: 0.8, y: 0.85 }, // END
    speed: 0.038
  },
  sonars: [],
  movingObstacles: [],
  staticObstacles: [
    {
      name: "wallUp",
      centerPosition: { x: 0.5, y: 1 },
      size: { x: 1, y: 0.01 }
    },
    {
      name: "wallDown",
      centerPosition: { x: 0.5, y: 0 },
      size: { x: 1, y: 0.01 }
    },
    {
      name: "wallLeft",
      centerPosition: { x: 0, y: 0.5 },
      size: { x: 0.01, y: 1 }
    },
    {
      name: "wallRight",
      centerPosition: { x: 1, y: 0.5 },
      size: { x: 0.01, y: 1 }
    },
    {
      name: "bag1",
      centerPosition: { x: 0.09, y: 0.42 },
      size: { x: 0.1, y: 0.1 }
    },
    {
      name: "bag2",
      centerPosition: { x: 0.51, y: 0.96 },
      size: { x: 0.1, y: 0.1 }
    },
  ]
}

export default config;
