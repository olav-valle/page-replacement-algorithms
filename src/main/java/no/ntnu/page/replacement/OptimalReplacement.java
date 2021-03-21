package no.ntnu.page.replacement;

import java.util.List;

/**
 * Optimal Replacement algorithm
 * Fill in your code in this class!
 */
public class OptimalReplacement extends ReplacementAlgorithm {

    // TODO - add some state variables here, if you need any

    @Override
    protected void reset() {
        // TODO - do preparation/initialization here, if needed
    }

    @Override
    public int process(String referenceString) {
        List<Integer> pageReferences = Tools.stringToArray(referenceString);

        //if page reference list is empty,
        // or tool method somehow returned null,
        // we do zero replacements.
        if (pageReferences == null || pageReferences.size() == 0) return 0;

        int replacements = 0; // How many page replacements made

        //traverse pageReferences
        for (int i = 0; i < pageReferences.size(); i++) {
            // if page is not in frame
            if (!isLoaded(pageReferences.get(i))) {
                //miss!

                //find victim frame using prediction method
                int victimIndex = findFarthestFuturePage(pageReferences, i + 1);

                // put page in frame
                boolean replaced = false;
                if (victimIndex == -1) {
                    // we can use any frame, and chose frame number 0 as default.
                    replaced = pageIn(0, pageReferences.get(i));
                } else if (victimIndex < frames.length) { //safety check for valid frame number
                    // use frame number returned by prediction method
                    replaced = pageIn(victimIndex, pageReferences.get(i));
                }

                if (replaced) replacements++; // if pageIn returned true, we've had a page replacement

                System.out.println(getFrameStatus());
            }
        }

        return replacements;
    }

    /**
     * Checks if any of the page numbers currently in a frame are present among the
     * remaining values of the input array (the "future").
     * <p>
     * Returns the index of the "victim frame", i.e. the index in frames[] of the
     * page number that is used "farthest ahead in the future", or is never used again.
     * <p>
     * Returns -1 if if none of the pages currently in frames are used again, i.e.
     * all frames are valid "victim frame" candidates.
     * <p>
     * Example:
     * current pages in frames: [2, 4, 5]
     * remaining page numbers in pages: [3, 4, 1, 0, 2, 0, 4, 5, 1, 4]
     * Of the page numbers in currently in frames (2, 4, 5), 5 is first used
     * again after both 4 and 2 are used again. Therefore, the return value is the
     * index of 5 in the frames[] array, i.e. 2.
     * <p>
     * In another example, if the current pages in frames were [6, 9, 7],
     * neither of those three numbers come up again in the input array.
     * Therefore, the return would be -1, indicating that either
     */
    private int findFarthestFuturePage(List<Integer> pageReferences, int nextPageRefIndex) {
        int victimCandidateIdx = -1; //
        int farthestPageIndex = nextPageRefIndex; // We start looking at page numbers after the current page ref
        int maxPageIndex = pageReferences.size() - 1; // Highest index in pageRefs, so we know when to stop looking

        // check for empty frames.
        int emptyFrame = findEmptyFrame(); // -1 if all frames are full
        if (emptyFrame != -1) {
            return emptyFrame;
        }

        //traverse frames
        int f = 0;
        boolean abort = false;
        while (f < frames.length && !abort) {

            //traverse pageRefs from current index onwards
            int p = nextPageRefIndex;
            boolean finished = false;
            while (p < pageReferences.size() && !finished) {
                if (frames[f] == pageReferences.get(p)) {

                    if (p > farthestPageIndex) {
                        // we start next frame iteration at current location, since any page ref
                        //  before this one is a worse choice
                        farthestPageIndex = p;
                        victimCandidateIdx = f;
                    }
                    finished = true;

                } else if (p == maxPageIndex) { //end of pageRefs list
                    // page number in frames[f] is never used again,
                    // and is the best candidate.
                    victimCandidateIdx = f;
                    finished = true;
                    abort = true;
                }
                p++;
            }
            f++;
        }
        return victimCandidateIdx;
    }


    /**
     * Finds index of the first empty frame.
     *
     * @return Index of first empty frame, or -1 if all frames are loaded.
     */
    private int findEmptyFrame() {
        for (int f = 0; f < frames.length; f++) {
            if (frames[f] == -1) return f;
        }
        return -1;
    }
}