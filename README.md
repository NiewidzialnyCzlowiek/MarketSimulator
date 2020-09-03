# Market Simulator
## Overview
Market simulator is a simple JavaFX application that mimics a real stock exchange. The application uses multithreading to provide truly independent work of the investors!
It allows you to create...

![Creating investor in the application](./readmeAssets/create_investor.gif)

...and delete assets, markets, companies and investors.

![Deleting investor in the application](./readmeAssets/delete_investor.gif)

You can obseve historical values of the market goods and compare them.

![Shared plot view](./readmeAssets/plot.png)

There is also a serialization system that allows you to save the state of the simulator.

![Performing application backup](./readmeAssets/backup.gif)

## Running the project
To build and run the project just use proper maven command: ```mvn compile javafx:run```
