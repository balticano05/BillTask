package org.bil.task.seat;

public final class SeatInformation {

    private final long id;
    private final String row;
    private final String seat;
    private final String sector;
    private final String rowName;
    private final String seatName;
    private final String sectorName;

    public SeatInformation(long id, String row,
                           String seat, String sector,
                           String rowName, String seatName,
                           String sectorName) {
        this.id = id;
        this.row = row;
        this.seat = seat;
        this.sector = sector;
        this.rowName = rowName;
        this.seatName = seatName;
        this.sectorName = sectorName;
    }

    public String getSector() {
        return sector;
    }

    public String getSectorName() {
        return sectorName;
    }

    public String getRow() {
        return row;
    }

    public String getRowName() {
        return rowName;
    }

    public String getSeat() {
        return seat;
    }

    public String getSeatName() {
        return seatName;
    }

    /**
     * Returns full sector description, e.g. "SectorName" or "sector sectorName".
     */
    public String getFullSector() {

        if (sector != null && sectorName != null) {

            if ("сектор".equalsIgnoreCase(sector)) {
                return sectorName;
            } else {
                return sector + " " + sectorName;
            }

        }

        return "";
    }

    /**
     * Returns full row description, e.g. "Ряд rowName".
     */
    public String getFullRow() {

        if (row != null && rowName != null) {
            return row + " " + rowName;
        }

        return "";
    }

    /**
     * Returns full seat description, e.g. "Место seatName".
     */
    public String getFullSeat() {

        if (seat != null && seatName != null) {
            return seat + " " + seatName;
        }

        return "";
    }

}