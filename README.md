# Water Simulation

This is a piece of Java software that I have written that allows you to simulate a water network. In order to write this
I have written my own sprite library for Java (JSprite), which allows me to elegantly build complex interfaces.

### A note on the accuracy of the simulation.
It may look sometimes that the flow is not going as intended (particular empty pipes where you think there shouldn't be).
However, this is intentional. The flow algorithm that I am using is 'maximum flow' algorithm, that just tries to maximise
the amount of flow between source and sink.

### Places that you might want to look
There's a lot going on in the code of this project. Here are a few of the highlights:
* NetworkManager.java - Network orchestrates the entire application, and handles window drawing
* To understand NetworkManager you might want to check out JSpriteCanvas, JSprite, and some of the many implementors
of JSpriteVisual.
* JSpriteLine - I'm particularly happy with the code here that checks whether or not a point is on the line.
* The different MouseHandler classes for all of the sprites. Most of the code in there is pretty simple, but elegantly
functional.
* EdmondsKarp - This is where I implemented all the code for actually simulating the flow of water.
