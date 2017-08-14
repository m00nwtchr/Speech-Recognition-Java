# About
Java library for: speech recognition, query processing and speech to text in 116 lines!

Yep you heard it right 116 (without comments) lines is the simplest implementation of this library with:
1. Speech to text
2. Text recognition / query processing and matching
2. Text to speech to respond to users!

PS please note that i'm not a native English speaker but i try to make my code as much English-people-friendly as i can

# Prerequisties:
1. Java 8 or newer
2. Google Speech API Key (only if you want speech to text)
3. FFMpeg (optional without it lib can only use LINEAR16 .wav and x-flac .flac files with SpeechToText Note: if on arm FFmpeg integration is disabled because of some weird behaviour i will try to fix that)

# How to get google speech api key?
1. Go to https://groups.google.com/a/chromium.org/forum/?fromgroups#!forum/chromium-dev and click join
2. Go back to https://console.developers.google.com/ create new project or select existing one go to APIs & Services click enable apis and services at the top and search Speech API click the one with grey Private sign and click enable
3. Then go to credentials on the left side of the screen now click create credentials and then api key then click close and copy your api key
4. You have it you can use it with my library!

#Examples:
You will find examles in src/examples dir!
