/**
 * Copyright (C) 2023 Alcatraz323 <alcatraz32323@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package com.razer.parts;

 import android.content.Context;
 import android.media.AudioManager;
 import android.service.quicksettings.Tile;
 import android.service.quicksettings.TileService;
 
 public class VolumePanelTileService extends TileService {
     public VolumePanelTileService() { }
 
     @Override
     public void onStartListening() {
         super.onStartListening();
         final Tile tile = getQsTile();
         tile.setState(Tile.STATE_ACTIVE);
         tile.updateTile();
     }
 
     @Override
     public void onClick() {
         AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
         audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                     AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
     }
 }