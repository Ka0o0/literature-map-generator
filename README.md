# Biblatex Markdown Table Generator

Generate a grouped Markdown table from your biblatex bibliography.

## Features

- Automatically parse your .bib file
- Generate a Markdown table
- Group the literature by a custom key
- Optionally create a link to your literature document if it is stored in **Jabref** style (file key)

## Usage

Simple run the program as following

```
program.jar inputFilePath groupingKey literatureRootPath
```

## Example

**Note:** Links are broken on purpose


| Literature Scope | Literatue |
| --- | --- |
| **PID in Something** |   |
|   | [Potential Induced Degradatioon of Solar Cells and Panels](./files/Pingel2010%20-%20Potential%20Induced%20Degradatioon%20of%20Solar%20Cells%20and%20Panels.pdf) |
| **N/A** |   |
|   | [CRYSTALLINE SI SOLAR CELLS AND MODULES FEATURING EXCELLENT STABILITY AGAINST POTENTIAL-INDUCED DEGRADATION](./files/Nagel2011%20-%20CRYSTALLINE%20SI%20SOLAR%20CELLS%20aND%20MODULES%20FEATURING%20EXCELLENT%20STABILITY%20aGAINST%20POTENTIAL%20INDUCED%20DEGRADATION.pdf) |
|   | [Comparison of Analysis Methods for the Calculation of Degredation Rates of Different Photovoltaic Technologies](./files/Phinikarides2013%20-%20Comparison%20of%20Analysis%20Methods%20for%20the%20Calculation%20of%20Degredation%20Rates%20of%20Different%20Photovoltaic%20Technologies.pdf) |
|   | [Early degradation of silicon PV modules and guaranty conditions](./files/Munoz2011%20-%20Early%20Degradation%20of%20Silicon%20PV%20Modules%20and%20Guaranty%20Conditions.pdf) |
