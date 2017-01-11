package com.knapsack.dao;

import java.io.File;

import com.knapsack.structure.InputFileData;

public interface KnapsackDao {
	InputFileData getDataFromFile(File file);
}
