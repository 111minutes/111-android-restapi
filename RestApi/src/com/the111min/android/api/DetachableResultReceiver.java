/*
 * Copyright 2011 Google Inc.
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

package com.the111min.android.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.the111min.android.api.response.ResponseReceiver;

/**
 * Proxy {@link ResultReceiver} that offers a listener interface that can be
 * detached. Useful for when sending callbacks to a {@link Service} where a
 * listening {@link Activity} can be swapped out during configuration changes.
 */
class DetachableResultReceiver extends ResultReceiver {

    private ResponseReceiver mReceiver;

    public DetachableResultReceiver(Handler handler) {
        super(handler);
    }

    public void clearReceiver() {
        mReceiver = null;
    }

    public void setReceiver(ResponseReceiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            int token = resultData.getInt(RequestService.EXTRA_TOKEN, -1);
            switch (resultCode) {
                case RequestService.STATUS_REQUEST_SUCCESS:
                    mReceiver.onRequestSuccess(token, resultData);
                    break;

                case RequestService.STATUS_REQUEST_FAILED:
                    mReceiver.onRequestFailure(token, resultData);
                    break;

                case RequestService.STATUS_ERROR:
                    final Exception exception = (Exception) resultData.getSerializable(
                            RequestService.EXTRA_RESPONSE_EXCEPTION);
                    mReceiver.onError(token, exception);
                    break;

            }
        }
    }
}
