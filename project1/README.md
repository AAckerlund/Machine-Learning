Some things to note
1. Some data sets contain incomplete data points, we can do one of 2 things.
    1. Replace invalid data points with random (acceptable) values. (This is the easier of the 2 options)
    2. Replace invalid data points with what we precieved to be an accurate data point.
        1. ex: valid data points are between 0 and 10, 90% of those values are between 2 and 3, so we generate a random value that is weighted to be between 2 and 3.
    3. either way we need to document our choice.
        1. I think we should go with random values to start and then later we can try and use seemingly accurate values.
2. This program is multithreaded
    1. This should cut down on runtime significantly (Assuming this program takes as long as it has in previous semesters).
    2. To accomplish this you do the following:
        1. Extend Thread in the class you want to multithread
        2. Call [class instance].start()
        3. in [class] make a ``` public void run() ``` function.
            1. This is what [class instance].start() calls