/**
 * Copyright (c) 2015-2018 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be used
 * for internal evaluation purposes or commercial use strictly subject to separate licensee
 * agreement between you and TomTom. If you are the licensee, you are only permitted to use
 * this Software in accordance with the terms of your license agreement. If you are not the
 * licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */
package com.tomtom.online.sdk.samples.cases.route.matrix;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tomtom.online.sdk.routing.data.matrix.MatrixRoutingResponse;

public class MatrixRoutesTableViewModel extends ViewModel {

    private MutableLiveData<MatrixRoutingResponse> matrixRoutingResponseLiveData = new MutableLiveData<>();

    public void saveMatrixRoutingResponse(MatrixRoutingResponse matrixRoutingResponse) {
        matrixRoutingResponseLiveData.postValue(matrixRoutingResponse);
    }

    public LiveData<MatrixRoutingResponse> getLastMatrixRoutingResponse() {
        return matrixRoutingResponseLiveData;
    }
}
