package com.pg85.otg.bukkit.metrics;

import com.pg85.otg.bukkit.OTGPlugin;
import com.pg85.otg.util.helpers.MetricsHelper;
import org.bukkit.Bukkit;

import java.io.IOException;

/**
 * Create an instance of this during onEnable. After some time,
 * stats will be sent to mcstats.org.
 *
 */
public class BukkitMetricsHelper extends MetricsHelper
{
    private final OTGPlugin plugin;

    public BukkitMetricsHelper(OTGPlugin plugin)
    {
        this.plugin = plugin;

        // Wait five seconds for worlds to load
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                startMetrics();
            }
        }, 5 * 20);
    }

    private void startMetrics()
    {
        calculateBiomeModes(plugin.worlds.values());

        // Thanks to the slightly different package names,
        // this code had to be copy/pasted from the Forge side.
        // When you update this method, also check the Forge class!
        try
        {
            Metrics metrics = new Metrics(plugin);

            Graph usedBiomeModesGraph = metrics.createGraph("Biome modes used");

            usedBiomeModesGraph.addPlotter(new Plotter("Normal")
            {
                @Override
                public int getValue()
                {
                    return normalMode;
                }
            });
            usedBiomeModesGraph.addPlotter(new Plotter("FromImage")
            {
                @Override
                public int getValue()
                {
                    return fromImageMode;
                }
            });
            usedBiomeModesGraph.addPlotter(new Plotter("Default")
            {
                @Override
                public int getValue()
                {
                    return vanillaMode;
                }
            });
            usedBiomeModesGraph.addPlotter(new Plotter("BeforeGroups")
            {
                @Override
                public int getValue()
                {
                    return beforeGroupsBiomeMode;
                }
            });
            usedBiomeModesGraph.addPlotter(new Plotter("OldGenerator")
            {
                @Override
                public int getValue()
                {
                    return oldBiomeMode;
                }
            });
            usedBiomeModesGraph.addPlotter(new Plotter("Custom / Unknown")
            {
                @Override
                public int getValue()
                {
                    return customMode;
                }
            });

            metrics.start();
        } catch (IOException e)
        {
            // Failed to submit stats
        }
    }
}
