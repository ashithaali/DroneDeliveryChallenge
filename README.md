# Drone Delivery Application

The drone delivery application, schedules drones that need to carry packages to customer location in a way that customer wait time is minimal.  This is achieved by maximizing the NPS(Net Promoter Score) for any given orders.

### Statements from POC Objective

  - Drone delivers from 6:00 AM to 10:00 PM.
  - The town is a perfect grid.
  - The drone-launch center is at the center of the grid.
  - The input file is sorted in the order of timestamp.

### Assumptions

  -  The grid is a grid of odd numbers.
  -  Input file is input.txt(The order file should be available in the file path given as input).
  -  Drone default delivery time is 5 minutes.

### NPS Algorithm

- The application read the order details from the input file specified in the path given and put them in separate lists (order, coordinates and order time). Linked list is the data structure used for storing order details. 

- Calculate drone-launch center coordinates using below formula.
```
    x=(n/2)+0.5
    y=(m/3)+0.5
```
(x,y) is center coordinates of the grid.

- Check drone current time is less than 10:00 PM or all the orders in the list are processed. If yes, go to Step 4 else go to Step 12

- Retrieve all order coordinate details, which are less than drone current time and from the current index of the order coordinate details and store in a temporary array of coordinates. Current list index is maintained as they are processed.

- Calculate distance of each coordinate in the temporary array of coordinates from the drone launch coordinates using below formula.

```
    Distance=((x-x1) or (x1-x)) + ((y-y1) or(y1-y))
```

- Create a distance map by having key as the index of the temporary array of coordinates and value as the distance of the coordinates.

- Sort the distance map by value (distance) to get the order of coordinates the drone should deliver the orders.

- Drone pulls the order from order list based on the index (key in sorted distance map) and deliver the order. Drone delivery time for the order is calculated base

```
    Drone delivery time = current drone time + default drone time (minutes)+(2* distance)(seconds)
```

- Default drone time is incremented by 5 minutes for every order. That is for first order if default drone time is 5 minutes then 2nd order it will be 10 minutes.

- Current drone time is set to drone delivery time.

- Drone order and drone delivery order are stored in separate lists.

- Step 10: Step 8 and Step 9 are repeated until the orders are delivered to all the coordinates in the sorted distance map.

- Go to Step 3.(To pull next set of orders to be processed)

- Calculate promoters, neutrals and detractors by calculating the difference between order delivered time and ordered time.

- If the time taken to deliver is 1 hour or less, then promoters count is incremented.

- If the time taken to deliver is more than 1 hour and less than 4 hours then count of neutral is incremented.

- If the time taken to deliver is 4 hours or more then count of detractors are incremented.


#### Calculate NPS

```
NPS= %Promoters -%Detractors
%Promoters = (Promoters count*100)/total Orders
%Detractors = (Detractors count*100)/total Orders
```

- Write the list of orders along with order delivered time and NPS to the output file.

### Result:

With the above algorithm, an NPS of 100 was achieved for the sample input in the requirement.

### Build Instructions

```

```

### Test Scenarios
