package sagma.core.sound3d;

/* 
 * Copyright (c) 2004 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
//import org.lwjgl.LWJGLUtil;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
//import org.lwjgl.openal.ALC;
import org.lwjgl.util.*;

public class Mixer3D {

	String fileLocation;
	String fileLocation2;
	String fileLocation3;
	//indexes of the sounds
	public static final int SOUND_1 = 0;
	public static final int SOUND_2 = 1;
	public static final int SOUND_3 = 2;
	
	public static final int NUM_BUFFERS = 3;
	
	public static final int NUM_SOURCES = 3;
	
	boolean unloaded = true;

	IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);


	IntBuffer source = BufferUtils.createIntBuffer(NUM_BUFFERS);


	FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3*NUM_BUFFERS);


	FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3*NUM_BUFFERS);


	FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });


	FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

	FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });  

	public Mixer3D(String fileLoc, String fileLoc2, String fileLoc3) {
		// CRUCIAL!
		// any buffer that has data added, must be flipped to establish its position and limits
		sourcePos.flip();
		sourceVel.flip();
		listenerPos.flip();
		listenerVel.flip();
		listenerOri.flip();
		fileLocation = fileLoc;
		fileLocation2 = fileLoc2;
		fileLocation3 = fileLoc3;
		
		init();
		//System.setProperty("org.lwjgl.util.Debug","true");
	}


	int loadALData() {
		// Load wav data into a buffer.
		AL10.alGenBuffers(buffer);

		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;
		//import sound 1
		try 
		{
			File file = new File(fileLocation);
			URL url = file.toURI().toURL();
			WaveData waveFile = WaveData.create(url);
			AL10.alBufferData(buffer.get(SOUND_1), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return AL10.AL_FALSE;
		}	
		//import sound 2
		try 
		{
			File file = new File(fileLocation);
			URL url = file.toURI().toURL();
			WaveData waveFile = WaveData.create(url);
			AL10.alBufferData(buffer.get(SOUND_2), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return AL10.AL_FALSE;
		}	
		//import sound 3
		try 
		{
			File file = new File(fileLocation);
			URL url = file.toURI().toURL();
			WaveData waveFile = WaveData.create(url);
			AL10.alBufferData(buffer.get(SOUND_3), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return AL10.AL_FALSE;
		}	

		// Bind the buffer with the source.
		AL10.alGenSources(source);

		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;

		AL10.alSourcei(source.get(SOUND_1), AL10.AL_BUFFER,   buffer.get(SOUND_1) );
		AL10.alSourcef(source.get(SOUND_1), AL10.AL_PITCH,    1.0f          );
		AL10.alSourcef(source.get(SOUND_1), AL10.AL_GAIN,     1.0f          );
		AL10.alSource (source.get(SOUND_1), AL10.AL_POSITION, sourcePos     );
		AL10.alSource (source.get(SOUND_1), AL10.AL_VELOCITY, sourceVel     );
		
		AL10.alSourcei(source.get(SOUND_2), AL10.AL_BUFFER,   buffer.get(SOUND_2) );
		AL10.alSourcef(source.get(SOUND_2), AL10.AL_PITCH,    1.0f          );
		AL10.alSourcef(source.get(SOUND_2), AL10.AL_GAIN,     1.0f          );
		AL10.alSource (source.get(SOUND_2), AL10.AL_POSITION, sourcePos     );
		AL10.alSource (source.get(SOUND_2), AL10.AL_VELOCITY, sourceVel     );
		
		AL10.alSourcei(source.get(SOUND_3), AL10.AL_BUFFER,   buffer.get(SOUND_3) );
		AL10.alSourcef(source.get(SOUND_3), AL10.AL_PITCH,    1.0f          );
		AL10.alSourcef(source.get(SOUND_3), AL10.AL_GAIN,     1.0f          );
		AL10.alSource (source.get(SOUND_3), AL10.AL_POSITION, sourcePos     );
		AL10.alSource (source.get(SOUND_3), AL10.AL_VELOCITY, sourceVel     );

		// Do another error check and return.
		if(AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;

		return AL10.AL_FALSE;
	}  

	void setListenerValues() {
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}  

	void killALData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}  

	public void init()
	{
		// Initialize OpenAL and clear the error bit.
	    try {
			AL.create(null, 15, 22050, true);
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}
		AL10.alGetError();

		// Load the wav data.
		if(loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
			return;
		}

		setListenerValues();
	}
	public void play(int i) 
	{
		switch(i)
		{
		case SOUND_1:
        AL10.alSourcePlay(source.get(SOUND_1));
        break;
		case SOUND_2:
	        AL10.alSourcePlay(source.get(SOUND_2));
	        break;
		case SOUND_3:
	        AL10.alSourcePlay(source.get(SOUND_3));
	        break;
		}
	}
}
