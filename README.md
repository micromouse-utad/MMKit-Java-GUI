# MMKit-Java-GUI

A GUI interface built in Java to interface via Bluetooth with the [MMKit PH code](https://github.com/micromouse-utad/MMKit-PH).

Using the Bluecove 2.1.1 Bluetooth library http://www.bluecove.org/
Using the JogAmp 2.3.2 JOGL (OpenGL Java binding library) https://jogamp.org/ 
and openGL version 3.3 for 3d rendering
3d model (E 45 aircraft) by Dennis "3dhaupt" Haupt, taken from his Free 3d Models, on free3d.com

The app allows the discovery of bluetooth devices, synchronizing to the desired device. Using port 1 as the bluetooth connection port 
The app will then wait for the micro-mouse to broadcast its positional and wall detection data, to display it.

Saved / paired devices can be managed directly from the app. A previously synched device will automatically be saved to the "known devices" list and can be deleted from said list. 

At any point a connection with the micro mouse can be ended using the file menu disconnect option

Runs can be saved to the disc or replay with the file menu save and replay option.

During a run or replay the app will present a 3d view alongside a 2d over the top representation.
The 3d view can be altered from the contextual menu.

Much like live runs, replays can be ended with the file menu disconnect // or end.

IMPORTANT:
The message broadcasted by the micromouse needs to be a string with the given convention.

XY:D W=000
    X and Y in hexadecimal giving the position of the mouse.
    The app expects a 16x16 grid, with position ranging from 0 to 255.
    D the direction written as N W E S for North West East South
    W=010 where W stands for walls, and the sensor data comes in binary from left to right.
    the first 0 here represents no wall on the left sensor, the 1 a wall detected by the front sensor
    and the trailing 0 represent no wall being detected by the right sensor.


