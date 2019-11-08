const config = {
  floor: {
      size: { x: 34, y: 30 }
  },
  player: {
      //position: { x: 0.5, y: 0.5 },		//CENTER
      position: { x: 0.065, y: 0.075 },		//INIT
      //position: { x: 0.8, y: 0.85 },		//END
      speed: 0.038
  },
  sonars: [
//        {
//            name: "sonar1",
//            position: { x: 0.064, y: 0.05 },
//            senseAxis: { x: false, y: true }
//        },
//        {
//            name: "sonar2",
//            position: { x: 0.94, y: 0.88},
//            senseAxis: { x: true, y: false }
//        }
   ],
  movingObstacles: [
     // {
     //     name: "moving-obstacle-1",
     //     position: { x: .5, y: .4 },
     //     directionAxis: { x: true, y: false },
     //     speed: 1,
     //     range: 8
     // }
     // {
     //     name: "moving-obstacle-2",
     //     position: { x: .5, y: .2 },
     //     directionAxis: { x: true, y: true },
     //     speed: 2,
     //     range: 2
     // }
  ],
  staticObstacles: [
//        {
//            name: "wall",
//            centerPosition: { x: 0.5, y: 0.9},
//            size: { x: 0.01, y: 0.01}
//        },
      {
          name: "wallUp",
          centerPosition: { x: 0.5, y: 1},
          size: { x: 1, y: 0.01}
      },
      {
          name: "wallDown",
          centerPosition: { x: 0.5, y: 0},
          size: { x: 1, y: 0.01}
      },
      {
          name: "wallLeft",
          centerPosition: { x: 0, y: 0.5},
          size: { x: 0.01, y: 1}
      },
      {
          name: "wallRight",
          centerPosition: { x: 1, y: 0.5},
          size: { x: 0.01, y: 1}
      },
      {
          name: "bag1",
          centerPosition: { x: 0.4, y: 0.38 },
          size: { x: 0.12, y: 0.045 }
      },
      {
          name: "bag2",
          centerPosition: { x: 0.75, y: 0.66 },
          size: { x: 0.05, y: 0.045 }
      },
  ]
}

export default config;
