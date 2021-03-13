Ganimed - a GIF animation editor
==
*Don´t trust the name, it´s a creator not yet an editor.*

This program creates a GIF file from a series of single image files.

![](ThankYouVeryMuch.gif)



__Features:__

![](Ganimed_screenshot.png)

* easy cropping, resizing and framerate adjusting

* animation preview in GUI, no 'adjust - create GIF - check GIF' cycle

* extra delay for last image

* GIF file size estimation

  


__Preparation:__

First, you need a folder which contains all the image files you want to convert to a GIF animation.

If your 'source' is a video file, use your tool of choice to extract the single image files.

<u>Example:</u>

ffmpeg.exe -y -i TheVideoFile.avi -f image2 -c:v png "image%03d.png"




__Ganimed usage:__

![](DemoTime.gif)

Image folder
--

The image folder, must not contain any other files.



Image FPS
--

If your 'source' is a video file, enter the original playback speed in Frames Per Second.

After selecting the first image folder, Ganimed sets this value to 25,- thats just a guess and ok for PAL videos. If your video player says '50 FPS', he´s probably talking about interlace frames, so again, 25 is correct.

For a series of stop-motion-photos, 3 is probably a good value to start and play around.

This value is only used to calculate the correct GIF-anim-delay after loading the images.



Crop top/bottom/left/right
--

Just try. Easy, isn´t it?



Resize
--

Again, no explanation needed :-)



GIF FPS
--

The Frames Per Second of the GIF file.

For a source animation of 25 FPS, and a GIF framerate of 12 FPS, each second image will be skipped.

Play around with this value to get both a good animation and a short filesize.

Changing this value recalculates the GIF delay.

For a series of stop-motion-images, set this value to maximum, which is 'ImagesFPS', to avoid skipping of images. Adjust the animation speed by changing 'ImagesFPS' and 'GIF delay'.



GIF delay
--

The animation delay, the time in milliseconds between two images. This value gets calculated when changing GIF FPS.



End delay
--

To add some extra delay to the last image, before replaying the animation.



__Additional notes:__

- WYSIWYG or not WYSIWYG, that´s the question.
  Concerning size and playback speed, yes, at least if your CPU isn´t overloaded with other duties.

  Concerning image quality, no. While the original images (probably truecolor) are shown in Ganimed, they have to be converted to 256-color-palette-images (after cropping) at the end, when creating the GIF file.

- Color reduction
  While testing, i noticed that Java´s color quantizer often delivered...let´s say unexpected (not to say ugly) results.

  Thats why Ganimed uses it´s own algorithm, based on 'Median Cut'.

  I called it the GPAC algorithm (Gif Piece A Change). I´m a bit indecisive but probably will create an own GitHub project for that.



